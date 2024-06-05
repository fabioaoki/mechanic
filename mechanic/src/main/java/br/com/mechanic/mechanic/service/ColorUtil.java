package br.com.mechanic.mechanic.service;

import java.util.HashMap;
import java.util.Map;

public class ColorUtil {
    private static final Map<String, String> colorMap;
    private static final Map<String, String> hexToColorMap;

    static {
        colorMap = new HashMap<>();
        hexToColorMap = new HashMap<>();

        // Mapeamento de cores
        colorMap.put("vermelho", "#FF0000");
        colorMap.put("verde", "#008000");
        colorMap.put("azul", "#0000FF");
        colorMap.put("amarelo", "#FFFF00");
        colorMap.put("preto", "#000000");
        colorMap.put("branco", "#FFFFFF");

        // Mapeamento inverso
        colorMap.forEach((key, value) -> hexToColorMap.put(value, key));
    }

    public static boolean isValidColor(String color) {
        if (color == null) {
            return false;
        }
        // Verifica se a cor é válida em português
        return colorMap.containsKey(color.toLowerCase());
    }

    public static String getColorHex(String color) {
        if (color == null || color.trim().isEmpty()) {
            throw new IllegalArgumentException("Color cannot be null or empty");
        }

        // Se a cor está em português, retorna o hexadecimal equivalente
        color = color.trim().toLowerCase();
        if (colorMap.containsKey(color)) {
            return colorMap.get(color);
        }

        throw new IllegalArgumentException("Invalid color: " + color);
    }

    public static String getColorName(String hex) {
        if (hex == null || hex.trim().isEmpty()) {
            throw new IllegalArgumentException("Hexadecimal color cannot be null or empty");
        }

        // Se o hexadecimal está no mapeamento, retorna o nome da cor
        hex = hex.trim().toUpperCase();
        if (hexToColorMap.containsKey(hex)) {
            String color = hexToColorMap.get(hex);
            return color.substring(0, 1).toUpperCase() + color.substring(1).toLowerCase();
        }

        throw new IllegalArgumentException("Invalid hexadecimal color: " + hex);
    }
}
