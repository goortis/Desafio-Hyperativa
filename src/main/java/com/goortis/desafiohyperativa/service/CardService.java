package com.goortis.desafiohyperativa.service;

import com.goortis.desafiohyperativa.model.Card;
import com.goortis.desafiohyperativa.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    /**
     * Salva um cartão no banco de dados, armazenando tanto o número criptografado
     * quanto o hash do número para consultas.
     *
     * @param cardNumber O número do cartão a ser salvo.
     * @return O cartão salvo.
     */
    public Card saveCard(String cardNumber) {
        Card card = new Card();
        card.setCardNumber(cardNumber);
        card.setCardNumberHash(generateHash(cardNumber));
        return cardRepository.save(card);
    }

    /**
     * Processa um arquivo TXT e salva os cartões.
     *
     * @param file O arquivo contendo os dados.
     * @return Lista dos cartões salvos.
     * @throws Exception Em caso de erro.
     */
    public List<Card> saveCardsFromFile(MultipartFile file) throws Exception {
        List<Card> cards = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            List<String> lines = br.lines().collect(Collectors.toList());
            for (String line : lines) {
                if (line.startsWith("C")) {
                    if (line.length() >= 26) {
                        String cardNumber = line.substring(7, 26).trim();
                        cards.add(saveCard(cardNumber));
                    } else {
                        throw new IllegalArgumentException("Linha de registro inválida: " + line);
                    }
                }
            }
        }
        return cards;
    }

    /**
     * Gera um hash SHA-256 para o número do cartão.
     */
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
}
