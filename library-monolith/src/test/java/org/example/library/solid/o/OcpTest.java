package org.example.library.solid.o.solution;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SOLID O — Open/Closed Principle")
class OcpTest {

    private BookRater rater;

    @BeforeEach
    void setUp() {
        rater = new BookRater(List.of(
                new FictionRatingStrategy(),
                new ScienceRatingStrategy(),
                new HistoryRatingStrategy()
        ));
    }

    @Test @DisplayName("Fiction : formule de calcul correcte")
    void fictionRating() {
        double rating = rater.calculateRating("fiction", 20);
        assertEquals(Math.min(5, 20 * 0.1 + 2.0), rating, 0.001);
    }

    @Test @DisplayName("Science : formule de calcul correcte")
    void scienceRating() {
        double rating = rater.calculateRating("science", 10);
        assertEquals(Math.min(5, 10 * 0.15 + 2.5), rating, 0.001);
    }

    @Test @DisplayName("History : formule de calcul correcte")
    void historyRating() {
        double rating = rater.calculateRating("history", 5);
        assertEquals(Math.min(5, 5 * 0.08 + 3.0), rating, 0.001);
    }

    @Test @DisplayName("La note ne dépasse jamais 5")
    void ratingCappedAt5() {
        double rating = rater.calculateRating("fiction", 1000);
        assertEquals(5.0, rating, 0.001);
    }

    @Test @DisplayName("Type inconnu → IllegalArgumentException")
    void unknownTypeThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> rater.calculateRating("unknown", 10));
    }

    @Test @DisplayName("OCP : ajout d'un nouveau type sans modifier BookRater")
    void addNewTypeWithoutModifyingBookRater() {
        // On crée la nouvelle stratégie et on l'enregistre
        RatingStrategy biographyStrategy = new RatingStrategy() {
            @Override public double calculate(int reviewCount) {
                return Math.min(5, reviewCount * 0.12 + 1.8);
            }
            @Override public String getBookType() { return "biography"; }
        };

        assertFalse(rater.supports("biography"), "Ne doit pas exister avant l'ajout");
        rater.register(biographyStrategy);
        assertTrue(rater.supports("biography"), "Doit exister après l'ajout");

        double rating = rater.calculateRating("biography", 15);
        assertEquals(Math.min(5, 15 * 0.12 + 1.8), rating, 0.001);
    }
}
