package com.empresa.util;

public class Avatar {
    public static String obtenerIniciales(String nombreCompleto) {
        if (nombreCompleto == null || nombreCompleto.isBlank()) return "";
        String[] partes = nombreCompleto.trim().split("\\s+");
        String ini = partes[0].substring(0, 1).toUpperCase();
        if (partes.length > 1) {
            ini += partes[partes.length - 1].substring(0, 1).toUpperCase();
        }
        return ini;
    }
    public static String generarColorDesdeNombre(String nombre) {
        int hash = nombre.hashCode();
        int r = (hash >> 16) & 0xFF;
        int g = (hash >> 8) & 0xFF;
        int b = hash & 0xFF;

        // Para evitar colores muy oscuros
        r = Math.max(r, 80);
        g = Math.max(g, 80);
        b = Math.max(b, 80);

        return String.format("#%02X%02X%02X", r, g, b);
    }
}
