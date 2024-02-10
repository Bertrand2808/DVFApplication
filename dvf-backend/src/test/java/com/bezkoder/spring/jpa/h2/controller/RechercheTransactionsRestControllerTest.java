package com.bezkoder.spring.jpa.h2.controller;

import com.bezkoder.spring.jpa.h2.exception.ParametresManquantsException;
import com.bezkoder.spring.jpa.h2.service.JmsMessageSender;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.core.annotation.Order;
import org.springframework.web.util.UriComponentsBuilder;

import static org.assertj.core.api.Assertions.assertThat;



@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
    @Order(1)
    void rechercherTransaction() throws Exception {
        Double latitude = 20.0;
        Double longitude = 20.0;
        Double rayon = 30.0;

        String url = UriComponentsBuilder.fromPath("/api/transactions")
                .queryParam("longitude", longitude)
                .queryParam("latitude", latitude)
                .queryParam("rayon", rayon)
                .toUriString();

        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    assertThat(responseBody).isEqualTo("Recherche en cours, veuillez attendre la notification.");
                });
    }

    @Test
    @Order(2)
    void rechercherTransaction_missingParameters() throws Exception {
        Double longitude = 20.0;
        Double rayon = 30.0;

        String url = UriComponentsBuilder.fromPath("/api/transactions")
                .queryParam("longitude", longitude)
                .queryParam("rayon", rayon)
                .toUriString();

        mockMvc.perform(get(url))
                .andExpect(status().isBadRequest());
    }
    @Test
    @Order(3)
    void rechercherTransactionWithMissingParameters() throws Exception {
        Double latitude = null;
        Double longitude = 20.0;
        Double rayon = 30.0;

        String url = UriComponentsBuilder.fromPath("/api/transactions")
                .queryParam("longitude", longitude)
                .queryParam("latitude", latitude)
                .queryParam("rayon", rayon)
                .toUriString();

        mockMvc.perform(get(url))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    assertThat(responseBody).isEqualTo("Les paramètres latitude, longitude et rayon sont obligatoires.");
                });
    }



    @Test
    @Order(4)
    void rechercherTransaction_invalidParameters() throws Exception {
        String latitude = "abc";
        Double longitude = 20.0;
        Double rayon = 30.0;

        String url = UriComponentsBuilder.fromPath("/api/transactions")
                .queryParam("longitude", longitude)
                .queryParam("latitude", latitude)
                .queryParam("rayon", rayon)
                .toUriString();

        mockMvc.perform(get(url))
                .andExpect(status().isBadRequest());
    }
}