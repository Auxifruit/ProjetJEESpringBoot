package com.example.testspring.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashPswdUtil {

    public static String hashPassword(String password) {
        try {
            // Créer une instance de MessageDigest pour SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Convertir le mot de passe en tableau de bytes
            byte[] encodedHash = digest.digest(password.getBytes());

            // Convertir le tableau de bytes en chaîne hexadécimale
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            // Renvoyer une version tronquée à 50 caractères
            return hexString.substring(0, 49);

        } catch (NoSuchAlgorithmException e) {
            // Gérer l'erreur si l'algorithme n'est pas disponible
            throw new RuntimeException("Erreur : Algorithme SHA-256 non disponible", e);
        }
    }
}
