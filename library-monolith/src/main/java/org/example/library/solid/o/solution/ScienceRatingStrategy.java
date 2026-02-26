package org.example.library.solid.o.solution;

public class ScienceRatingStrategy implements RatingStrategy {
    @Override
    public double calculate(int reviewCount) {
        return Math.min(5, reviewCount * 0.15 + 2.5);
    }

    @Override
    public String getBookType() { return "science"; }
}
