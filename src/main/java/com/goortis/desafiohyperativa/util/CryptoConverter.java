package com.goortis.desafiohyperativa.util;

import org.jasypt.util.text.AES256TextEncryptor;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class CryptoConverter implements AttributeConverter<String, String> {

    private final AES256TextEncryptor encryptor = new AES256TextEncryptor();

    public CryptoConverter() {
        encryptor.setPassword("chaveSegura");
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return attribute == null ? null : encryptor.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return dbData == null ? null : encryptor.decrypt(dbData);
    }
}
