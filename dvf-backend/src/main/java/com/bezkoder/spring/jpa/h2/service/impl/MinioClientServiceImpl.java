package com.bezkoder.spring.jpa.h2.service.impl;

import com.bezkoder.spring.jpa.h2.exception.MinioUploadException;
import com.bezkoder.spring.jpa.h2.service.MinioClientService;
import io.minio.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

/**
 * Classe MinioClientServiceImpl permettant d'implémenter les méthodes pour uploader un fichier PDF sur Minio.
 */
@Component
@Service
public class MinioClientServiceImpl implements MinioClientService {

    private final MinioClient minioClient;

    public MinioClientServiceImpl(@Value("${minio.url}") String minioUrl,
                                  @Value("${minio.accessKey}") String accessKey,
                                  @Value("${minio.secretKey}") String secretKey) {
        this.minioClient = MinioClient.builder()
                .endpoint(minioUrl)
                .credentials(accessKey, secretKey)
                .build();
    }

    @Override
    public void uploadPdf(String bucketName, String objectName, byte[] content) throws MinioUploadException {
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(new ByteArrayInputStream(content), content.length, -1)
                            .contentType("application/pdf")
                            .build());
        } catch (Exception e) {
            throw new MinioUploadException("Failed to upload PDF to Minio", e);
        }
    }
}