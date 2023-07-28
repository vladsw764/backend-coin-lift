package com.coinlift.backend.services.s3;

import lombok.extern.log4j.Log4j2;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
@Log4j2
public class S3Service {

    private final S3Client s3;

    public S3Service(S3Client s3) {
        this.s3 = s3;
    }

    public void putObject(String bucketName, String key, byte[] file) {

        byte[] reducedImage = reduceImageQuality(file);

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        s3.putObject(objectRequest, RequestBody.fromBytes(reducedImage));
    }

    public byte[] getObject(String bucketName, String key) {
        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        try {
            return s3.getObject(objectRequest).readAllBytes();
        } catch (IOException e) {
            log.error("Error while getting data with key: {}", key);
            throw new RuntimeException(e);
        }
    }

    public void deleteObject(String bucketName, String key) {
        DeleteObjectRequest objectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        log.info("trying to remove the image from s3");
        s3.deleteObject(objectRequest);
    }

    public byte[] reduceImageQuality(byte[] imageBytes) {
        int targetWidth = 683;
        int targetHeight = 407;

        try {
            log.info("start - reduceImageQuality(): " + LocalDateTime.now());
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
            BufferedImage image = ImageIO.read(inputStream);

            BufferedImage resizedImage = Scalr.resize(image, Scalr.Method.BALANCED, targetWidth, targetHeight);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(resizedImage, "jpeg", outputStream);

            byte[] compressedImage = outputStream.toByteArray();
            log.info("end - reduceImageQuality(): " + LocalDateTime.now());

            return compressedImage;
        } catch (IOException e) {
            log.error("Error reducing image quality");
            throw new RuntimeException(e);
        }
    }

}
