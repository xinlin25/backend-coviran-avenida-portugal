package com.example.demo.Proyecto.Service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmailService {

    @Value("${brevo.api.key}")
    private String apiKey;

    public void enviarCorreo(
            String destino,
            String asunto,
            String contenido) {

        RestTemplate restTemplate = new RestTemplate();

        String url = "https://api.brevo.com/v3/smtp/email";

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.set("api-key", apiKey);

        Map<String, Object> body = Map.of(

                "sender", Map.of(
                        "name", "Recuperar Coviran",
                        "email", "xinlinchenzhang@gmail.com"),

                "to", List.of(
                        Map.of("email", destino)),

                "subject", asunto,

                "textContent", contenido);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                String.class);
    }
}