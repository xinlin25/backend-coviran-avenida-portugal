package com.example.demo.Proyecto.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final TypeReference<List<String>> STRING_LIST_TYPE = new TypeReference<>() {};

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        List<String> imagenes = normalizar(attribute);
        if (imagenes.isEmpty()) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(imagenes);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("No se pudieron guardar las imagenes del producto", e);
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return Collections.emptyList();
        }

        String data = dbData.trim();
        try {
            if (data.startsWith("[")) {
                return normalizar(objectMapper.readValue(data, STRING_LIST_TYPE));
            }

            if (data.startsWith("\"")) {
                return normalizar(List.of(objectMapper.readValue(data, String.class)));
            }
        } catch (JsonProcessingException e) {
            return normalizar(List.of(data));
        }

        if (data.startsWith("{") && data.endsWith("}")) {
            String contenido = data.substring(1, data.length() - 1);
            return normalizar(List.of(contenido.split(",")));
        }

        return normalizar(List.of(data));
    }

    private List<String> normalizar(List<String> imagenes) {
        if (imagenes == null) {
            return Collections.emptyList();
        }

        return imagenes.stream()
                .filter(imagen -> imagen != null && !imagen.isBlank())
                .map(String::trim)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
