package org.example.library.patterns.singleton;

/**
 * SINGLETON PATTERN — Approche B : Enum Singleton (recommandé par Joshua Bloch)
 *
 * LibraryConfig est la configuration globale de la bibliothèque.
 *
 * Pourquoi l'Enum Singleton est SUPÉRIEUR au Singleton classique ?
 *
 *  1. Sérialisation garantie : Java garantit qu'une Enum ne sera jamais
 *     désérialisée en plusieurs instances (le Singleton classique peut être
 *     cassé par la sérialisation/désérialisation).
 *
 *  2. Protection contre la réflexion : on ne peut pas créer une nouvelle
 *     instance d'une Enum via la réflexion (le Singleton classique peut être
 *     cassé avec Constructor.newInstance()).
 *
 *  3. Thread-safe nativement : l'initialisation d'une Enum est garantie
 *     thread-safe par la JVM (pas besoin de synchronized).
 *
 *  4. Code minimal : aucun boilerplate nécessaire.
 */
public enum LibraryConfig {

    INSTANCE;

    // Configuration de la bibliothèque
    private String libraryName        = "Bibliothèque YNOV";
    private int    maxBooksPerUser    = 5;
    private int    loanDurationDays   = 14;
    private double dailyFineAmount    = 0.20;
    private boolean reservationEnabled = true;

    // --- Getters ---
    public String getLibraryName()         { return libraryName; }
    public int    getMaxBooksPerUser()      { return maxBooksPerUser; }
    public int    getLoanDurationDays()     { return loanDurationDays; }
    public double getDailyFineAmount()      { return dailyFineAmount; }
    public boolean isReservationEnabled()  { return reservationEnabled; }

    // --- Setters (config mutable en runtime) ---
    public void setLibraryName(String libraryName)             { this.libraryName = libraryName; }
    public void setMaxBooksPerUser(int maxBooksPerUser)        { this.maxBooksPerUser = maxBooksPerUser; }
    public void setLoanDurationDays(int loanDurationDays)      { this.loanDurationDays = loanDurationDays; }
    public void setDailyFineAmount(double dailyFineAmount)     { this.dailyFineAmount = dailyFineAmount; }
    public void setReservationEnabled(boolean enabled)         { this.reservationEnabled = enabled; }

    @Override
    public String toString() {
        return "LibraryConfig{" +
               "name='"              + libraryName         + '\'' +
               ", maxBooks="         + maxBooksPerUser      +
               ", loanDays="         + loanDurationDays     +
               ", dailyFine="        + dailyFineAmount      +
               ", reservations="     + reservationEnabled   +
               '}';
    }
}
