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

    /**
     * Uploads an object to the specified S3 bucket.
     *
     * @param bucketName The name of the S3 bucket.
     * @param key        The unique key to identify the object in the bucket.
     * @param file       The byte array representing the file to be uploaded.
     */
    public void putObject(String bucketName, String key, byte[] file) {

        byte[] reducedImage = reduceImageQuality(file);

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        s3.putObject(objectRequest, RequestBody.fromBytes(reducedImage));
    }

    /**
     * Retrieves an object from the specified S3 bucket.
     *
     * @param bucketName The name of the S3 bucket.
     * @param key        The unique key to identify the object in the bucket.
     * @return The byte array representing the retrieved object.
     * @throws RuntimeException if an error occurs while getting the object.
     */
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

    /**
     * Deletes an object from the specified S3 bucket.
     *
     * @param bucketName The name of the S3 bucket.
     * @param key        The unique key to identify the object in the bucket.
     */
    public void deleteObject(String bucketName, String key) {
        DeleteObjectRequest objectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        log.info("trying to remove the image from s3");
        s3.deleteObject(objectRequest);
    }

    /**
     * Reduces the quality of an image represented by a byte array.
     *
     * @param imageBytes The byte array representing the image.
     * @return The byte array of the compressed image.
     * @throws RuntimeException if an error occurs while reducing the image quality.
     */
    private byte[] reduceImageQuality(byte[] imageBytes) {
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
