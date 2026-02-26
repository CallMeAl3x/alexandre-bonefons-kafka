package org.example.library.patterns.prototype;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pattern 4 — Prototype (deep copy)")
class UserPreferencesPrototypeTest {

    private UserPreferences original;

    @BeforeEach
    void setUp() {
        original = new UserPreferences("Fantasy", "fr", 14);
        original.addFavoriteAuthor("Tolkien");
        original.addFavoriteAuthor("Le Guin");
        original.setNotificationPref("email", true);
        original.setNotificationPref("sms", false);
    }

    @Test
    @DisplayName("Le clone est une instance différente de l'original")
    void cloneIsNotSameReference() {
        UserPreferences copy = original.clone();
        assertNotSame(original, copy, "clone() doit retourner un nouvel objet");
    }

    @Test
    @DisplayName("Le clone a les mêmes valeurs que l'original")
    void cloneHasSameValues() {
        UserPreferences copy = original.clone();
        assertEquals(original.getPreferredGenre(),  copy.getPreferredGenre());
        assertEquals(original.getLanguage(),         copy.getLanguage());
        assertEquals(original.getMaxLoanDuration(),  copy.getMaxLoanDuration());
        assertEquals(original.getFavoriteAuthors(),  copy.getFavoriteAuthors());
        assertEquals(original.getNotificationPrefs(), copy.getNotificationPrefs());
    }

    @Test
    @DisplayName("Modifier le genre du clone ne change pas l'original (primitif/String)")
    void modifyingCloneGenreDoesNotAffectOriginal() {
        UserPreferences copy = original.clone();
        copy.setPreferredGenre("Science-Fiction");

        assertEquals("Fantasy", original.getPreferredGenre(),
                "Le genre de l'original ne doit pas changer");
        assertEquals("Science-Fiction", copy.getPreferredGenre());
    }

    @Test
    @DisplayName("Ajouter un auteur au clone ne modifie pas la liste de l'original (deep copy List)")
    void addingAuthorToCloneDoesNotAffectOriginal() {
        UserPreferences copy = original.clone();
        copy.addFavoriteAuthor("Terry Pratchett");

        assertEquals(2, original.getFavoriteAuthors().size(),
                "L'original doit toujours avoir 2 auteurs");
        assertEquals(3, copy.getFavoriteAuthors().size(),
                "Le clone doit avoir 3 auteurs");
        assertFalse(original.getFavoriteAuthors().contains("Terry Pratchett"));
    }

    @Test
    @DisplayName("Modifier une préférence de notif du clone ne change pas l'original (deep copy Map)")
    void modifyingNotifPrefOnCloneDoesNotAffectOriginal() {
        UserPreferences copy = original.clone();
        copy.setNotificationPref("sms", true);

        assertFalse(original.getNotificationPrefs().get("sms"),
                "La préférence SMS de l'original doit rester false");
        assertTrue(copy.getNotificationPrefs().get("sms"));
    }

    @Test
    @DisplayName("Modifier l'original après clonage ne change pas le clone")
    void modifyingOriginalDoesNotAffectClone() {
        UserPreferences copy = original.clone();
        original.addFavoriteAuthor("Nouveau auteur");
        original.setPreferredGenre("Horreur");

        assertFalse(copy.getFavoriteAuthors().contains("Nouveau auteur"));
        assertEquals("Fantasy", copy.getPreferredGenre());
    }
}
