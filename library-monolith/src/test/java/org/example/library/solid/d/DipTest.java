package org.example.library.solid.d.solution;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SOLID D — Dependency Inversion Principle")
class DipTest {

    // -----------------------------------------------------------------------
    // Fakes légers pour tester sans infrastructure réelle
    // -----------------------------------------------------------------------

    static class FakeNotification implements NotificationService {
        final List<String> sent = new ArrayList<>();
        @Override
        public void send(String recipient, String message) {
            sent.add(recipient + ":" + message);
        }
    }

    static class FakeRepository implements DataRepository {
        final java.util.Map<String, String> store = new java.util.HashMap<>();
        @Override public void save(String key, String value) { store.put(key, value); }
        @Override public String find(String key) { return store.get(key); }
    }

    // -----------------------------------------------------------------------
    // Tests
    // -----------------------------------------------------------------------

    @Test @DisplayName("createOrder persiste la commande via le repository")
    void createOrderSavesToRepository() {
        FakeRepository fakeRepo = new FakeRepository();
        OrderService svc = new OrderService(new FakeNotification(), fakeRepo);

        svc.createOrder("user1", "book-42");

        assertEquals("book-42", fakeRepo.store.get("order_user1"),
                "La commande doit être persistée avec la clé 'order_{userId}'");
    }

    @Test @DisplayName("createOrder envoie une notification")
    void createOrderSendsNotification() {
        FakeNotification fakeNotif = new FakeNotification();
        OrderService svc = new OrderService(fakeNotif, new FakeRepository());

        svc.createOrder("user1", "book-42");

        assertEquals(1, fakeNotif.sent.size(), "Une seule notification doit être envoyée");
        assertTrue(fakeNotif.sent.get(0).contains("user1"));
    }

    @Test @DisplayName("getOrder retourne la commande après création")
    void getOrderAfterCreate() {
        OrderService svc = new OrderService(new FakeNotification(), new FakeRepository());
        svc.createOrder("user2", "book-99");

        assertEquals("book-99", svc.getOrder("user2"));
    }

    @Test @DisplayName("InMemoryRepository stocke et retrouve les données")
    void inMemoryRepositoryWorks() {
        InMemoryRepositoryImpl repo = new InMemoryRepositoryImpl();
        repo.save("k1", "v1");

        assertEquals("v1", repo.find("k1"));
        assertNull(repo.find("unknown"));
    }

    @Test @DisplayName("DIP : on peut changer l'implémentation sans modifier OrderService")
    void swapImplementations() {
        FakeNotification smsNotif  = new FakeNotification();
        FakeNotification emailNotif = new FakeNotification();
        FakeRepository   repo      = new FakeRepository();

        // Avec SMS
        new OrderService(smsNotif, repo).createOrder("user3", "book-1");
        // Avec Email
        new OrderService(emailNotif, repo).createOrder("user3", "book-2");

        // OrderService n'a pas changé — seuls les collaborateurs ont changé
        assertEquals(1, smsNotif.sent.size());
        assertEquals(1, emailNotif.sent.size());
    }
}
