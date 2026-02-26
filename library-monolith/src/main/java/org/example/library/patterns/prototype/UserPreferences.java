package org.example.library.patterns.prototype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PROTOTYPE PATTERN
 *
 * Objectif : Cloner des objets sans connaître leur type exact,
 *            en copiant un objet existant comme modèle (prototype).
 *
 * Contexte : Un lecteur crée son profil de préférences une fois.
 *            Un autre lecteur peut copier ce profil et le personnaliser
 *            sans affecter l'original.
 *
 * POURQUOI UNE DEEP COPY EST IMPORTANTE ?
 * Une shallow copy copie les références des objets internes (listes, maps...).
 * Si on modifie la copie, on modifie aussi l'original → bug difficile à détecter.
 * La deep copy crée de nouvelles instances pour chaque objet imbriqué,
 * rendant l'original totalement indépendant du clone.
 */
public class UserPreferences implements Cloneable {

    private String  preferredGenre;
    private String  language;
    private int     maxLoanDuration;       // en jours
    private boolean emailNotifications;
    private boolean smsNotifications;

    // Objets imbriqués → nécessitent une deep copy
    private List<String>         favoriteAuthors;   // liste mutable
    private Map<String, Boolean> notificationPrefs; // map mutable

    public UserPreferences(String preferredGenre, String language, int maxLoanDuration) {
        this.preferredGenre      = preferredGenre;
        this.language            = language;
        this.maxLoanDuration     = maxLoanDuration;
        this.emailNotifications  = true;
        this.smsNotifications    = false;
        this.favoriteAuthors     = new ArrayList<>();
        this.notificationPrefs   = new HashMap<>();
    }

    // =====================================================================
    // DEEP COPY via clone()
    // =====================================================================

    /**
     * Clone PROFOND (deep copy) :
     * - Les primitifs et String sont copiés par valeur (safe).
     * - Les collections (List, Map) sont recréées → indépendantes de l'original.
     */
    @Override
    public UserPreferences clone() {
        try {
            // 1. Shallow copy des primitifs / String (immutables → safe)
            UserPreferences copy = (UserPreferences) super.clone();

            // 2. Deep copy des objets mutables imbriqués
            copy.favoriteAuthors   = new ArrayList<>(this.favoriteAuthors);
            copy.notificationPrefs = new HashMap<>(this.notificationPrefs);

            return copy;
        } catch (CloneNotSupportedException e) {
            // Ne peut pas arriver puisqu'on implémente Cloneable
            throw new AssertionError("Clone impossible", e);
        }
    }

    // --- Méthodes métier ---

    public void addFavoriteAuthor(String author) {
        favoriteAuthors.add(author);
    }

    public void setNotificationPref(String channel, boolean enabled) {
        notificationPrefs.put(channel, enabled);
    }

    // --- Getters / Setters ---

    public String  getPreferredGenre()       { return preferredGenre; }
    public String  getLanguage()             { return language; }
    public int     getMaxLoanDuration()      { return maxLoanDuration; }
    public boolean isEmailNotifications()    { return emailNotifications; }
    public boolean isSmsNotifications()      { return smsNotifications; }
    public List<String> getFavoriteAuthors() { return favoriteAuthors; }
    public Map<String, Boolean> getNotificationPrefs() { return notificationPrefs; }

    public void setPreferredGenre(String preferredGenre)        { this.preferredGenre = preferredGenre; }
    public void setLanguage(String language)                    { this.language = language; }
    public void setMaxLoanDuration(int maxLoanDuration)         { this.maxLoanDuration = maxLoanDuration; }
    public void setEmailNotifications(boolean emailNotifications) { this.emailNotifications = emailNotifications; }
    public void setSmsNotifications(boolean smsNotifications)   { this.smsNotifications = smsNotifications; }

    @Override
    public String toString() {
        return "UserPreferences{" +
               "genre='"            + preferredGenre    + '\'' +
               ", lang='"           + language          + '\'' +
               ", maxLoan="         + maxLoanDuration   +
               ", email="           + emailNotifications +
               ", sms="             + smsNotifications  +
               ", authors="         + favoriteAuthors   +
               ", notifPrefs="      + notificationPrefs +
               '}';
    }
}
