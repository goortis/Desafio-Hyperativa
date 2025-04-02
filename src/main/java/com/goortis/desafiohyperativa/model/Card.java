package com.goortis.desafiohyperativa.model;

import jakarta.persistence.*;
import com.goortis.desafiohyperativa.util.CryptoConverter;

@Entity
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Número do cartão criptografado
    @Convert(converter = CryptoConverter.class)
    @Column(name = "card_number", nullable = false, unique = true)
    private String cardNumber;

    // Hash do número do cartão para buscas
    @Column(name = "card_number_hash", nullable = false, unique = true)
    private String cardNumberHash;

    // Getters e setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCardNumber() {
        return cardNumber;
    }
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
    public String getCardNumberHash() {
        return cardNumberHash;
    }
    public void setCardNumberHash(String cardNumberHash) {
        this.cardNumberHash = cardNumberHash;
    }
}
