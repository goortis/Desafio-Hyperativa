package com.goortis.desafiohyperativa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goortis.desafiohyperativa.model.Card;
import com.goortis.desafiohyperativa.service.CardService;
import com.goortis.desafiohyperativa.repository.CardRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CardController.class)
@WithMockUser(username = "adm", roles = {"USER"})
public class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardRepository cardRepository;

    @MockBean
    private CardService cardService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetCard() throws Exception {
        Card card = new Card();
        card.setId(1L);
        card.setCardNumber("4456897999999999");
        Mockito.when(cardRepository.findByCardNumberHash(anyString()))
                .thenReturn(Optional.of(card));

        mockMvc.perform(get("/api/cards/4456897999999999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testInsertCard() throws Exception {
        Card card = new Card();
        card.setId(1L);
        card.setCardNumber("4456897999999999");

        Mockito.when(cardService.saveCard(any(String.class))).thenReturn(card);

        CardController.CardRequest request = new CardController.CardRequest();
        request.setCardNumber("4456897999999999");

        mockMvc.perform(post("/api/cards")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cardNumber").value("4456897999999999"));
    }

    @Test
    public void testUploadCards() throws Exception {
        String validLine = "C0000004456897999999999123"; // Linha com pelo menos 26 caracteres
        String fileContent = validLine + "\n" + "LinhaQueNaoComecaComC";
        MockMultipartFile file = new MockMultipartFile(
                "file", "cards.txt", "text/plain", fileContent.getBytes()
        );

        Card card = new Card();
        card.setId(1L);
        card.setCardNumber("4456897999999999123");
        Mockito.when(cardService.saveCardsFromFile(any())).thenReturn(Collections.singletonList(card));

        mockMvc.perform(multipart("/api/cards/upload")
                        .file(file)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Inserção realizada com sucesso")));
    }
}
