package org.example.library.solid.s.solution;

/**
 * ✅ SRP — Responsabilité 1 : Validation des données utilisateur.
 * Seule cette classe sait ce qui constitue un username/email valide.
 */
public class UserValidator {

    public boolean isValidUsername(String username) {
        return username != null && username.length() >= 3;
    }

    public boolean isValidEmail(String email) {
        if (email == null) return false;
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}
