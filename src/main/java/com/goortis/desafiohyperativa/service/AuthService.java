package com.goortis.desafiohyperativa.service;

import com.goortis.desafiohyperativa.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private JwtUtil jwtUtil;

    public String authenticate(String username, String password) {
        if ("usuarioExemplo".equals(username) && "senhaSegura".equals(password)) {
            return jwtUtil.generateToken(username);
        }
        throw new RuntimeException("Credenciais inválidas");
    }
}