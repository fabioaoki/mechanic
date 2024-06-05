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
