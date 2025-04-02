package com.goortis.desafiohyperativa.service;

import com.goortis.desafiohyperativa.model.Card;
import com.goortis.desafiohyperativa.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardService cardService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveCardsFromFile_success() throws Exception {
        // Linha válida: deve ter pelo menos 26 caracteres
        String validLine = "C0000004456897999999999123"; // 26 caracteres
        String fileContent = validLine + "\n" + "LinhaQueNaoComecaComC";
        MockMultipartFile file = new MockMultipartFile(
                "file", "cards.txt", "text/plain", fileContent.getBytes(StandardCharsets.UTF_8)
        );

        when(cardRepository.save(any(Card.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<Card> result = cardService.saveCardsFromFile(file);
        assertNotNull(result);
        // Apenas a linha válida deve ser processada
        assertEquals(1, result.size());
        assertEquals("4456897999999999123", result.get(0).getCardNumber());
    }

    @Test
    public void testSaveCardsFromFile_invalidLine() {
        String invalidLine = "Cshort"; // comprimento insuficiente
        MockMultipartFile file = new MockMultipartFile(
                "file", "cards_invalid.txt", "text/plain", invalidLine.getBytes(StandardCharsets.UTF_8)
        );

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cardService.saveCardsFromFile(file);
        });
        assertTrue(exception.getMessage().contains("Linha de registro inválida"));
    }
}
