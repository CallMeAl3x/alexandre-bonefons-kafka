package org.example.library.solid.s.solution;

/**
 * ✅ SRP — Responsabilité 3 : Persistance des utilisateurs.
 * Seule cette classe sait comment sauvegarder/charger un utilisateur.
 */
public class UserRepository {

    public void save(String username) {
        System.out.println("[DB] Saving user: " + username);
    }

    public void delete(String username) {
        System.out.println("[DB] Deleting user: " + username);
    }
}
