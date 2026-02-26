package org.example.library.solid.s.solution;

/**
 * ✅ SRP — Responsabilité 2 : Notification des utilisateurs.
 * Seule cette classe sait comment contacter un utilisateur.
 */
public class UserNotifier {

    public void notify(String username, String message) {
        System.out.println("[NOTIF] Email to " + username + ": " + message);
    }
}
