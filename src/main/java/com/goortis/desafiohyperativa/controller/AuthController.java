package com.goortis.desafiohyperativa.controller;

import com.goortis.desafiohyperativa.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping
    public ResponseEntity<?> authenticate(@RequestBody AuthRequest authRequest) {
        try {
            String token = authService.authenticate(authRequest.getUsername(), authRequest.getPassword());
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
        }
    }

    // DTO para a requisição de autenticação
    public static class AuthRequest {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }
        public void setUsername(String username) {
            this.username = username;
        }
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
    }

    // DTO para a resposta de autenticação
    public static class AuthResponse {
        private String token;

        public AuthResponse(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }
        public void setToken(String token) {
            this.token = token;
        }
    }
}
