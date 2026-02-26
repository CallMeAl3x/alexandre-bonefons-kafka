package org.example.library.solid.d.solution;

/**
 * ✅ DIP — OrderService refactorisé avec injection de dépendances.
 *
 * OrderService ne connaît que les interfaces NotificationService et
 * DataRepository. Les implémentations concrètes sont injectées depuis
 * l'extérieur (constructeur) → couplage faible.
 *
 * On peut tester createOrder() avec un InMemoryRepository et un
 * FakeNotification sans aucune infrastructure réelle.
 */
public class OrderService {

    private final NotificationService notifier;
    private final DataRepository      repository;

    // ✅ Injection par constructeur : les dépendances viennent de l'extérieur
    public OrderService(NotificationService notifier, DataRepository repository) {
        this.notifier   = notifier;
        this.repository = repository;
    }

    public void createOrder(String userId, String bookId) {
        repository.save("order_" + userId, bookId);
        notifier.send(userId, "Commande créée avec succès pour le livre : " + bookId);
    }

    public String getOrder(String userId) {
        return repository.find("order_" + userId);
    }
}
