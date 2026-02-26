package org.example.library.solid.o.solution;

public class FictionRatingStrategy implements RatingStrategy {
    @Override
    public double calculate(int reviewCount) {
        return Math.min(5, reviewCount * 0.1 + 2.0);
    }

    @Override
    public String getBookType() { return "fiction"; }
}
