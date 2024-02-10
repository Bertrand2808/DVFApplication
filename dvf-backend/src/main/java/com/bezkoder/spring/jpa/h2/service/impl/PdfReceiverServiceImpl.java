package com.bezkoder.spring.jpa.h2.service.impl;

import com.bezkoder.spring.jpa.h2.config.MyWebSocketHandler;
import com.bezkoder.spring.jpa.h2.exception.ValueExtractionException;
import com.bezkoder.spring.jpa.h2.service.PdfGenerateurService;
import com.bezkoder.spring.jpa.h2.service.PdfReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@Service
public class PdfReceiverServiceImpl implements PdfReceiverService {
    private final Logger logger = Logger.getLogger(PdfReceiverServiceImpl.class.getName());
    private final PdfGenerateurService pdfGenerateurService;

    @Autowired
    public PdfReceiverServiceImpl(PdfGenerateurService pdfGenerateurService, MyWebSocketHandler myWebSocketHandler) {
        this.pdfGenerateurService = pdfGenerateurService;
    }

    /**
     * Reçoit un message contenant des informations pour générer un PDF.
     *
     * @param message le message
     */
    @Override
    @JmsListener(destination = "pdfQueue", containerFactory = "myFactory")
    public void receivePdf(String message) {
        if(logger.isLoggable(Level.INFO)) {
            logger.info("Received <" + message + ">");
        }
        double latitude = extractValue(message, "Latitude");
        double longitude = extractValue(message, "Longitude");
        double rayon = extractValue(message, "Rayon");
        if(logger.isLoggable(Level.INFO)) {
            logger.info("Latitude : " + latitude);
            logger.info("Longitude : " + longitude);
            logger.info("Rayon : " + rayon);
        }
        String fileName = "rapport_" + System.currentTimeMillis() + ".pdf";
        String path = "src/main/resources/" + fileName;
        pdfGenerateurService.enqueuePdfGeneration(path, latitude, longitude, rayon, objectName -> logger.info("PDF téléversé : " + objectName));
    }

    /**
     * Extrait la valeur associée à une clé dans un message.
     *
     * @param message le message
     * @param key la clé
     * @return la valeur associée à la clé
     * @throws ValueExtractionException si la valeur ne peut pas être extraite
     */
    private double extractValue(String message, String key) {
        String startTag = key + " : ";
        int startIndex = message.indexOf(startTag);
        if (startIndex != -1) {
            int endIndex = message.indexOf(',', startIndex);
            if (endIndex != -1) {
                String valueStr = message.substring(startIndex + startTag.length(), endIndex);
                try {
                    return Double.parseDouble(valueStr);
                } catch (NumberFormatException e) {
                    throw new ValueExtractionException("Erreur lors de l'extraction de la valeur.", e);
                }
            }
        }
        return 0.0;
    }
}