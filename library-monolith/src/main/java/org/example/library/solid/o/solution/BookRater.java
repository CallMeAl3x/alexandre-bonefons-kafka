package org.example.library.solid.o.solution;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ✅ OCP — BookRater refactorisé.
 *
 * Fermée à la modification : on ne touche JAMAIS à cette classe pour
 * un nouveau type de livre.
 * Ouverte à l'extension : on crée une nouvelle RatingStrategy et on
 * l'enregistre via register().
 *
 * RÉPONSE : Les stratégies rendent la classe OCP car le comportement
 * variable (le calcul) est extrait dans des classes séparées. BookRater
 * ne connaît que l'interface RatingStrategy, jamais les implémentations.
 */
public class BookRater {

    private final Map<String, RatingStrategy> strategies = new HashMap<>();

    public BookRater(List<RatingStrategy> strategyList) {
        strategyList.forEach(s -> strategies.put(s.getBookType(), s));
    }

    /**
     * Enregistre une nouvelle stratégie à chaud (sans modifier la classe).
     */
    public void register(RatingStrategy strategy) {
        strategies.put(strategy.getBookType(), strategy);
    }

    public double calculateRating(String bookType, int reviewCount) {
        RatingStrategy strategy = strategies.get(bookType);
        if (strategy == null) {
            throw new IllegalArgumentException("Type inconnu : " + bookType);
        }
        return strategy.calculate(reviewCount);
    }

    public boolean supports(String bookType) {
        return strategies.containsKey(bookType);
    }
}
