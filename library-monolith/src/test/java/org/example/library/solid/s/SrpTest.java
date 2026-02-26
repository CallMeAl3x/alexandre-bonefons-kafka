package org.example.library.solid.s.solution;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SOLID S — Single Responsibility Principle")
class SrpTest {

    private UserValidator validator;
    private UserRepository repository;
    private UserNotifier notifier;
    private UserStore store;
    private UserRegistrationService service;

    @BeforeEach
    void setUp() {
        validator  = new UserValidator();
        repository = new UserRepository();
        notifier   = new UserNotifier();
        store      = new UserStore();
        service    = new UserRegistrationService(validator, repository, notifier, store);
    }

    // --- UserValidator testé SEUL (pas de DB, pas de notif) ---

    @Test @DisplayName("Username valide (≥ 3 chars)")
    void validUsername() {
        assertTrue(validator.isValidUsername("alice"));
        assertTrue(validator.isValidUsername("ab1"));
    }

    @Test @DisplayName("Username invalide (< 3 chars ou null)")
    void invalidUsername() {
        assertFalse(validator.isValidUsername("ab"));
        assertFalse(validator.isValidUsername(null));
    }

    @Test @DisplayName("Email valide")
    void validEmail() {
        assertTrue(validator.isValidEmail("alice@example.com"));
        assertTrue(validator.isValidEmail("user.name+tag@domain.org"));
    }

    @Test @DisplayName("Email invalide")
    void invalidEmail() {
        assertFalse(validator.isValidEmail("not-an-email"));
        assertFalse(validator.isValidEmail(null));
    }

    // --- UserStore testé SEUL ---

    @Test @DisplayName("UserStore : add et contains")
    void userStoreAddContains() {
        store.add("bob");
        assertTrue(store.contains("bob"));
        assertFalse(store.contains("charlie"));
    }

    @Test @DisplayName("UserStore : getAll retourne une copie immuable")
    void userStoreGetAllIsImmutable() {
        store.add("alice");
        assertThrows(UnsupportedOperationException.class,
                () -> store.getAll().add("injected"));
    }

    // --- Orchestrateur ---

    @Test @DisplayName("register : succès avec données valides")
    void registerSuccess() {
        assertTrue(service.register("alice", "alice@example.com"));
        assertTrue(store.contains("alice"));
    }

    @Test @DisplayName("register : échec si username trop court")
    void registerFailsShortUsername() {
        assertFalse(service.register("ab", "ab@example.com"));
        assertFalse(store.contains("ab"));
    }

    @Test @DisplayName("register : échec si email invalide")
    void registerFailsInvalidEmail() {
        assertFalse(service.register("charlie", "not-an-email"));
        assertFalse(store.contains("charlie"));
    }

    @Test @DisplayName("unregister supprime l'utilisateur du store")
    void unregisterRemovesUser() {
        service.register("dave", "dave@example.com");
        service.unregister("dave");
        assertFalse(store.contains("dave"));
    }
}
