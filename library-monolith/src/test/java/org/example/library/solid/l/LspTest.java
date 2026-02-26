package org.example.library.solid.l.solution;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SOLID L — Liskov Substitution Principle")
class LspTest {

    @Test @DisplayName("Sparrow est substituable à Bird : eat() sans exception")
    void sparrowSubstitutableAsBird() {
        Bird bird = new Sparrow();
        assertDoesNotThrow(bird::eat);
        assertEquals("Sparrow", bird.getName());
    }

    @Test @DisplayName("Ostrich est substituable à Bird : eat() sans exception")
    void ostrichSubstitutableAsBird() {
        Bird bird = new Ostrich();
        assertDoesNotThrow(bird::eat, "L'autruche mange sans exception");
        assertEquals("Ostrich", bird.getName());
    }

    @Test @DisplayName("Sparrow implémente FlyingBird : fly() sans exception")
    void sparrowCanFly() {
        FlyingBird bird = new Sparrow();
        assertDoesNotThrow(bird::fly);
    }

    @Test @DisplayName("Eagle implémente FlyingBird : fly() sans exception")
    void eagleCanFly() {
        FlyingBird bird = new Eagle();
        assertDoesNotThrow(bird::fly);
    }

    @Test @DisplayName("Ostrich n'est PAS FlyingBird → détecté à la compilation (test conceptuel)")
    void ostrichIsNotFlyingBird() {
        // Si on pouvait écrire : FlyingBird fb = new Ostrich();
        // → erreur de COMPILATION, pas de runtime. C'est exactement le but du LSP.
        // On vérifie que Ostrich n'implémente pas FlyingBird via instanceof
        Bird ostrich = new Ostrich();
        assertFalse(ostrich instanceof FlyingBird,
                "Ostrich ne doit pas être un FlyingBird");
    }

    @Test @DisplayName("LSP : toutes les implementations de Bird gèrent eat() de manière cohérente")
    void allBirdsEatConsistently() {
        Bird[] birds = { new Sparrow(), new Ostrich(), new Eagle() };
        for (Bird bird : birds) {
            assertDoesNotThrow(bird::eat,
                    bird.getName() + " doit pouvoir manger sans exception");
            assertNotNull(bird.getName());
        }
    }
}
