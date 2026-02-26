package org.example.library.solid.o.solution;

public class HistoryRatingStrategy implements RatingStrategy {
    @Override
    public double calculate(int reviewCount) {
        return Math.min(5, reviewCount * 0.08 + 3.0);
    }

    @Override
    public String getBookType() { return "history"; }
}
