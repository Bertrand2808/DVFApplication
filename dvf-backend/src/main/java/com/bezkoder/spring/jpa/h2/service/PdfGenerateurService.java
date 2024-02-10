package com.bezkoder.spring.jpa.h2.service;

import java.util.function.Consumer;

/** Service pour générer un PDF */
public interface PdfGenerateurService {
    void enqueuePdfGeneration(String path, double latitude, double longitude, double rayon, Consumer<String> onPdfGenerated);
}