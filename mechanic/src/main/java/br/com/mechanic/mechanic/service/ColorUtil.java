package br.com.mechanic.mechanic.service;

import java.util.HashMap;
import java.util.Map;

public class ColorUtil {
    private static final Map<String, String> colorMap;

    static {
        colorMap = new HashMap<>();
        colorMap.put("vermelho", "#FF0000");
        colorMap.put("verde", "#008000");
        colorMap.put("azul", "#0000FF");
        colorMap.put("amarelo", "#FFFF00");
        colorMap.put("preto", "#000000");
        colorMap.put("branco", "#FFFFFF");
        colorMap.put("prata", "#C0C0C0");
        colorMap.put("cinza", "#808080");
        colorMap.put("marrom", "#A52A2A");
        colorMap.put("bege", "#F5F5DC");
        colorMap.put("roxo", "#800080");
        colorMap.put("rosa", "#FFC0CB");
        colorMap.put("laranja", "#FFA500");
        colorMap.put("dourado", "#FFD700");
        colorMap.put("bordô", "#800000");
        colorMap.put("azul marinho", "#000080");
        colorMap.put("verde oliva", "#808000");
        colorMap.put("vinho", "#722F37");
        colorMap.put("champanhe", "#F7E7CE");
    }

    public static boolean isValidColor(String color) {
        if (color == null) {
            return false;
        }
        return colorMap.containsKey(color.toLowerCase());
    }

    public static String formatColor(String color) {
        if (color == null || color.trim().isEmpty()) {
            throw new IllegalArgumentException("Color cannot be null or empty");
        }
        color = color.trim().toLowerCase();
        return color.substring(0, 1).toUpperCase() + color.substring(1).toLowerCase();
    }
}
