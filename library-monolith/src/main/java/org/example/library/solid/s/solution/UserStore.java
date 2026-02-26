package org.example.library.solid.s.solution;

import java.util.ArrayList;
import java.util.List;

/**
 * ✅ SRP — Responsabilité 4 : Gestion de la liste des utilisateurs en mémoire.
 * Seule cette classe gère la collection in-memory.
 */
public class UserStore {

    private final List<String> users = new ArrayList<>();

    public void add(String username) {
        users.add(username);
    }

    public void remove(String username) {
        users.remove(username);
    }

    public boolean contains(String username) {
        return users.contains(username);
    }

    public List<String> getAll() {
        return List.copyOf(users);
    }
}
