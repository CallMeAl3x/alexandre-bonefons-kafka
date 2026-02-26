package org.example.library.solid.d.solution;

import java.util.List;

/**
 * Point d'entrée public pour démontrer le DIP depuis l'extérieur du package.
 */
public class DipDemo {

    public static void run() {
        System.out.println("\n--- PRINCIPLE 5 : DIP ---");
        System.out.println("\n[❌ Problème] OrderService instancie EmailNotification et" +
                " DatabaseRepository directement → couplage fort, non testable");

        System.out.println("\n[✅ Solution] Injection de dépendances via interfaces");

        // Combinaison 1 : Email + DB réelle
        System.out.println("\nCombinaison EMAIL + DB :");
        OrderService svc1 = new OrderService(
                new EmailNotificationImpl(),
                new DatabaseRepositoryImpl()
        );
        svc1.createOrder("user1", "book-42");

        // Combinaison 2 : SMS + In-memory (parfait pour les tests)
        System.out.println("\nCombinaison SMS + InMemory :");
        InMemoryRepositoryImpl inMemRepo = new InMemoryRepositoryImpl();
        OrderService svc2 = new OrderService(
                new SmsNotificationImpl(),
                inMemRepo
        );
        svc2.createOrder("user2", "book-99");
        System.out.println("Récupération : " + svc2.getOrder("user2"));

        System.out.println("\n→ RÉPONSE : L'injection facilite les tests car on passe un " +
                "InMemoryRepository et un FakeNotifier → pas besoin de DB ni de serveur mail. " +
                "On teste la logique métier pure d'OrderService en totale isolation.");
    }
}
