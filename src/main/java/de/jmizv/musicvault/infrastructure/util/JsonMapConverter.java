package de.jmizv.musicvault.infrastructure.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Map;
import java.util.TreeMap;

@Converter
public class JsonMapConverter implements AttributeConverter<Map<String, Object>, String> {

    private final Gson gson = new GsonBuilder().create();

    @Override
    public String convertToDatabaseColumn(Map<String, Object> map) {
        return gson.toJson(map);
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String jsonString) {
        return gson.fromJson(jsonString, TreeMap.class);
    }
}