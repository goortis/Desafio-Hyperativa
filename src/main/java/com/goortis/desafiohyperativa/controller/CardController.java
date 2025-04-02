package com.goortis.desafiohyperativa.controller;

import com.goortis.desafiohyperativa.model.Card;
import com.goortis.desafiohyperativa.repository.CardRepository;
import com.goortis.desafiohyperativa.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardService cardService;

    @PostMapping
    public ResponseEntity<?> insertCard(@RequestBody CardRequest cardRequest) {
        Card saved = cardService.saveCard(cardRequest.getCardNumber());
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadCards(@RequestParam("file") MultipartFile file) {
        try {
            int count = cardService.saveCardsFromFile(file).size();
            return ResponseEntity.ok("Inserção realizada com sucesso: " + count + " registros.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar o arquivo");
        }
    }

    @GetMapping("/{cardNumber}")
    public ResponseEntity<?> getCard(@PathVariable String cardNumber) {
        String cardNumberHash = generateHash(cardNumber);
        Optional<Card> cardOpt = cardRepository.findByCardNumberHash(cardNumberHash);
        if (cardOpt.isPresent()) {
            return ResponseEntity.ok(Collections.singletonMap("id", cardOpt.get().getId()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cartão não encontrado");
    }

    private String generateHash(String cardNumber) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(cardNumber.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar hash", e);
        }
    }

    public static class CardRequest {
        private String cardNumber;
        public String getCardNumber() { return cardNumber; }
        public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    }
}
