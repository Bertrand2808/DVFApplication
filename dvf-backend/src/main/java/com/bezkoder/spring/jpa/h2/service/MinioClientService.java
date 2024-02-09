package com.bezkoder.spring.jpa.h2.service;

import com.bezkoder.spring.jpa.h2.exception.MinioUploadException;

/**
 * Interface MinioClientService permettant de définir les méthodes pour uploader un fichier PDF sur Minio.
 */
public interface MinioClientService {
    void uploadPdf(String bucketName, String objectName, byte[] content) throws MinioUploadException;
}
