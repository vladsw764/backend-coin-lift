package com.coinlift.filestorageservice.service;

import com.coinlift.filestorageservice.config.S3Buckets;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@Log4j2
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3;
    private final S3Buckets s3Buckets;

    public void putObject(String key, byte[] file) {
        byte[] reducedImage = reduceImageQuality(file);

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(s3Buckets.getCustomer())
                .key(key)
                .build();
        s3.putObject(objectRequest, RequestBody.fromBytes(reducedImage));
    }

    public byte[] getObject(String key) {
        String bucketName = s3Buckets.getCustomer();
        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        try {
            return s3.getObject(objectRequest).readAllBytes();
        } catch (IOException e) {
            log.error("can't get data from bucket: " + bucketName);
            throw new RuntimeException(e);
        }
    }

    public void deleteObject(String key) {
        DeleteObjectRequest objectRequest = DeleteObjectRequest.builder()
                .bucket(s3Buckets.getCustomer())
                .key(key)
                .build();
        log.info("trying to remove the image from s3");
        s3.deleteObject(objectRequest);
    }

    private byte[] reduceImageQuality(byte[] imageBytes) {
        int targetWidth = 683;
        int targetHeight = 407;
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
            BufferedImage image = ImageIO.read(inputStream);

            // Resize the image to the target dimensions
            Image resizedImage = image.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
            BufferedImage bufferedResizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = bufferedResizedImage.createGraphics();
            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            graphics2D.drawImage(resizedImage, 0, 0, targetWidth, targetHeight, null);
            graphics2D.dispose();

            // Compress the image with the specified quality
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedResizedImage, "jpeg", outputStream);

            return outputStream.toByteArray();
        } catch (IOException e) {
            log.error("Error reducing image quality");
            throw new RuntimeException(e);
        }
    }

}
