package com.bezkoder.spring.jpa.h2.controller;

import com.bezkoder.spring.jpa.h2.exception.ParametresManquantsException;
import com.bezkoder.spring.jpa.h2.service.JmsMessageSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RechercheTransactionsRestControllerTest {

    @InjectMocks
    private RechercheTransactionsRestController rechercheController;

    @Mock
    private JmsMessageSender jmsMessageSender;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(rechercheController).build();
    }

    @Test
    void rechercherTransaction() throws Exception {
        mockMvc.perform(get("/api/transactions")
                .param("latitude", "1.0")
                .param("longitude", "1.0")
                .param("rayon", "1.0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void rechercherTransaction_missingParameters() throws Exception {
        mockMvc.perform(get("/api/transactions")
                .param("latitude", "1.0")
                .param("longitude", "1.0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    void rechercherTransactionWithMissingParameters() {

        Double latitude = null;
        Double longitude = 20.0;
        Double rayon = 30.0;
        ParametresManquantsException exception = assertThrows(ParametresManquantsException.class, () -> {
            rechercheController.rechercherTransaction(latitude, longitude, rayon);
        });
        assertEquals("Les paramètres latitude, longitude et rayon sont obligatoires.", exception.getMessage());
        verifyNoInteractions(jmsMessageSender);
    }

    @Test
    void rechercherTransaction_invalidParameters() throws Exception {
        mockMvc.perform(get("/api/transactions")
                .param("latitude", "abc")
                .param("longitude", "1.0")
                .param("rayon", "1.0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}