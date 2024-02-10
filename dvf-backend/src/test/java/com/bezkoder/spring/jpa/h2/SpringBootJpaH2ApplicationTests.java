package com.bezkoder.spring.jpa.h2;
import com.bezkoder.spring.jpa.h2.config.MyWebSocketHandler;
import com.bezkoder.spring.jpa.h2.service.ImportationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.core.annotation.Order;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SpringBootJpaH2ApplicationTests {

	@Autowired
	private ImportationService ImportationService;

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private MyWebSocketHandler myWebSocketHandler;

	@Test
	@Order(1)
	public void shouldImportFileSuccessfully() throws Exception {
		assertNotNull(ImportationService);
		ImportationService.importCsvData();
		String longitude = "4.85";
		String latitude = "45.75";
		String rayon = "1000";

		String url = "/api/transactions?longitude=" + longitude + "&latitude=" + latitude + "&rayon=" + rayon;
		mockMvc.perform(get(url))
				.andExpect(status().isOk())
				.andExpect(result -> {
					String responseBody = result.getResponse().getContentAsString();
					assertThat(responseBody).isEqualTo("Recherche en cours, veuillez attendre la notification.");
				});

		// Establish a WebSocket connection
		/*WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
		StompSessionHandlerAdapter sessionHandler = new StompSessionHandlerAdapter() {};
		StompSession session = stompClient.connect("ws://localhost:8080/my-websocket-endpoint", sessionHandler).get();

		// Wait until the PDF notification is received through WebSocket
		await().atMost(10, SECONDS).untilAsserted(() -> {
			// Assert that the PDF is received through WebSocket
			// Implement your assertion logic here based on the content of the received PDF
		});*/
	}

	@Test
	@Order(2)
	void shouldReturnMissingParamException() throws Exception {
		String url = "/api/transactions";
		mockMvc.perform(get(url))
				.andExpect(status().isBadRequest())
				.andExpect(result -> {
					String responseBody = result.getResponse().getContentAsString();
					assertThat(responseBody).isEqualTo("Les paramètres latitude, longitude et rayon sont obligatoires.");
				});
	}

}
