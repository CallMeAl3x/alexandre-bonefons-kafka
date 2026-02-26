package org.example.library.solid.d.problem;

/**
 * ❌ PROBLÈME DIP : OrderService dépend de classes concrètes.
 * Il instancie lui-même EmailNotification et DatabaseRepository.
 * → Couplage fort, impossible à tester sans vrais email/DB.
 */
public class OrderService {
    private final EmailNotification emailService;
    private final DatabaseRepository dbRepo;

    public OrderService() {
        this.emailService = new EmailNotification();   // ❌ dépend du concret
        this.dbRepo       = new DatabaseRepository();  // ❌ dépend du concret
    }

    public void createOrder(String userId, String bookId) {
        dbRepo.save("order_" + userId, bookId);
        emailService.sendEmail(userId, "Order created successfully");
    }
}

class EmailNotification {
    public void sendEmail(String user, String message) {
        System.out.println("Email to " + user + ": " + message);
    }
}

class DatabaseRepository {
    public void save(String key, String value) {
        System.out.println("Saving to DB: " + key + " = " + value);
    }
}
