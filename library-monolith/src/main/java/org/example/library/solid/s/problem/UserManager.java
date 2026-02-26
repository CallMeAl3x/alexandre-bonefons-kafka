package org.example.library.solid.s.problem;

import java.util.*;

/**
 * ❌ PROBLÈME : UserManager viole le SRP.
 * Cette classe a 4 responsabilités différentes :
 *  1. Gérer la liste des utilisateurs (métier)
 *  2. Valider les données (validation)
 *  3. Envoyer des notifications (communication)
 *  4. Persister en base (infrastructure)
 */
public class UserManager {
    private List<String> users = new ArrayList<>();

    public void addUser(String username) {
        if (isValidUsername(username)) {
            users.add(username);
            System.out.println("User added: " + username);
        }
    }

    public void removeUser(String username) {
        users.remove(username);
    }

    public boolean isValidUsername(String username) {
        return username != null && username.length() >= 3;
    }

    public boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }

    public void notifyUser(String username, String message) {
        System.out.println("Email sent to " + username + ": " + message);
    }

    public void saveUserToDatabase(String username) {
        System.out.println("Saving " + username + " to database");
    }
}
