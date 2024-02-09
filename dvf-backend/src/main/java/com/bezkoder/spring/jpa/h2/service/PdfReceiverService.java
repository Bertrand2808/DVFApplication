package com.bezkoder.spring.jpa.h2.service;

/**
 * Service pour recevoir un message contenant des informations pour générer un PDF.

 */
public interface PdfReceiverService {
    void receivePdf(String message);
}