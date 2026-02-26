package org.example.library.solid.d.solution;

/**
 * ✅ DIP — Interfaces d'abstraction.
 *
 * OrderService dépend uniquement de ces interfaces,
 * jamais des implémentations concrètes.
 *
 * RÉPONSE : L'injection facilite les tests car on peut passer des mocks
 * ou des fakes sans toucher à OrderService : on teste la logique métier
 * pure, indépendamment de la DB ou des emails réels.
 */

// Abstraction pour les notifications
interface NotificationService {
    void send(String recipient, String message);
}

// Abstraction pour la persistance
interface DataRepository {
    void save(String key, String value);
    String find(String key);
}

// --- Implémentation 1 : Email ---
class EmailNotificationImpl implements NotificationService {
    @Override
    public void send(String recipient, String message) {
        System.out.println("[EMAIL] → " + recipient + " : " + message);
    }
}

// --- Implémentation 2 : SMS ---
class SmsNotificationImpl implements NotificationService {
    @Override
    public void send(String recipient, String message) {
        System.out.println("[SMS] → " + recipient + " : " + message);
    }
}

// --- Implémentation 1 : Base de données ---
class DatabaseRepositoryImpl implements DataRepository {
    @Override
    public void save(String key, String value) {
        System.out.println("[DB] save(" + key + ") = " + value);
    }
    @Override
    public String find(String key) {
        System.out.println("[DB] find(" + key + ")");
        return "result_" + key;
    }
}

// --- Implémentation 2 : In-memory (idéale pour les tests) ---
class InMemoryRepositoryImpl implements DataRepository {
    private final java.util.Map<String, String> store = new java.util.HashMap<>();

    @Override
    public void save(String key, String value) {
        store.put(key, value);
        System.out.println("[MEM] save(" + key + ") = " + value);
    }
    @Override
    public String find(String key) {
        return store.get(key);
    }
}
