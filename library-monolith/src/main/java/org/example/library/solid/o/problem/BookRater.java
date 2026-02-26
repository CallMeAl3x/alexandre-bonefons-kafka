package org.example.library.solid.o.problem;

/**
 * ❌ PROBLÈME : BookRater viole l'OCP.
 * Pour ajouter un type "biography", il faut MODIFIER cette classe.
 * Elle est ouverte à la modification → violation OCP.
 */
public class BookRater {

    public double calculateRating(String bookType, int reviewCount) {
        switch (bookType) {
            case "fiction":  return calculateFictionRating(reviewCount);
            case "science":  return calculateScienceRating(reviewCount);
            case "history":  return calculateHistoryRating(reviewCount);
            default:         return 0;
        }
    }

    private double calculateFictionRating(int reviewCount) {
        return Math.min(5, reviewCount * 0.1 + 2.0);
    }

    private double calculateScienceRating(int reviewCount) {
        return Math.min(5, reviewCount * 0.15 + 2.5);
    }

    private double calculateHistoryRating(int reviewCount) {
        return Math.min(5, reviewCount * 0.08 + 3.0);
    }
}
