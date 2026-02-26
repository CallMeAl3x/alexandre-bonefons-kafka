package org.example.library.patterns;

import org.example.library.patterns.builder.Book;
import org.example.library.patterns.factory.NotificationFactory;
import org.example.library.patterns.factory.NotificationService;
import org.example.library.patterns.prototype.UserPreferences;
import org.example.library.patterns.singleton.DatabaseConfig;
import org.example.library.patterns.singleton.LibraryConfig;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Runner de démonstration — Exercice 1 : Design Patterns
 *
 * Lancé automatiquement au démarrage de l'application Spring Boot.
 * Présente les 4 patterns implémentés.
 */
@Component
public class PatternsDemo implements CommandLineRunner {

    @Override
    public void run(String... args) {
        System.out.println("\n========================================");
        System.out.println("   EXERCICE 1 : DESIGN PATTERNS         ");
        System.out.println("========================================\n");

        demoBuilder();
        demoFactory();
        demoSingleton();
        demoPrototype();
    }

    // =====================================================================
    // PATTERN 1 : BUILDER
    // =====================================================================
    private void demoBuilder() {
        System.out.println("--- PATTERN 1 : BUILDER ---");

        // Livre complet avec tous les attributs optionnels
        Book complet = new Book.Builder("Le Seigneur des Anneaux", "J.R.R. Tolkien")
                .isbn("978-2-07-070322-8")
                .publisher("Gallimard")
                .pages(1200)
                .genre("Fantasy")
                .price(29.90)
                .publicationYear(1954)
                .available(true)
                .addTag("épique")
                .addTag("aventure")
                .addTag("classique")
                .description("L'histoire de la destruction de l'Anneau Unique.")
                .build();

        System.out.println("Livre complet  : " + complet);

        // Livre minimal : seulement les attributs obligatoires
        Book minimal = new Book.Builder("Clean Code", "Robert C. Martin")
                .genre("Informatique")
                .build();

        System.out.println("Livre minimal  : " + minimal);

        // Test de la validation
        try {
            new Book.Builder("", "Auteur").build();
        } catch (IllegalArgumentException e) {
            System.out.println("Validation OK  : " + e.getMessage());
        }

        System.out.println();

        /*
         * RÉPONSE : Avantage du Builder par rapport à un constructeur à 10 paramètres ?
         *
         * 1. LISIBILITÉ : chaque setter est nommé → on sait ce qu'on configure.
         *    new Book("titre", "auteur", null, null, 0, null, 0.0, 0, true, null, "")
         *    est illisible face au Builder.
         *
         * 2. OPTIONALITÉ : on ne fournit que les paramètres nécessaires,
         *    les autres gardent leur valeur par défaut.
         *
         * 3. IMMUABILITÉ : l'objet construit est final (les champs sont `final`),
         *    impossible de le modifier après coup → plus sûr.
         *
         * 4. VALIDATION centralisée dans les setters du Builder avant build().
         */
    }

    // =====================================================================
    // PATTERN 2 : FACTORY
    // =====================================================================
    private void demoFactory() {
        System.out.println("--- PATTERN 2 : FACTORY ---");

        // Approche B : Simple Factory — le client ne connaît que "EMAIL", "SMS", "PUSH"
        String[] types = {"EMAIL", "SMS", "PUSH"};
        for (String type : types) {
            NotificationService service = NotificationFactory.create(type);
            service.sendNotification("user@example.com", "Votre livre est disponible !");
        }

        // Test du type inconnu
        try {
            NotificationFactory.create("FAX");
        } catch (IllegalArgumentException e) {
            System.out.println("Type inconnu   : " + e.getMessage());
        }

        System.out.println();

        /*
         * RÉPONSE : L'Approche B (Simple Factory) est plus flexible car
         * un seul point de modification pour ajouter un nouveau type.
         * On ajoute juste un case dans le switch ET la nouvelle classe concrète.
         * Avec l'Approche A, il faut créer toute une nouvelle Factory class.
         */
    }

    // =====================================================================
    // PATTERN 3 : SINGLETON
    // =====================================================================
    private void demoSingleton() {
        System.out.println("--- PATTERN 3 : SINGLETON ---");

        // DatabaseConfig — lazy initialization
        DatabaseConfig db1 = DatabaseConfig.getInstance();
        DatabaseConfig db2 = DatabaseConfig.getInstance();
        System.out.println("db1 == db2 (même instance) : " + (db1 == db2));
        System.out.println("Database config : " + db1);

        // LibraryConfig — Enum Singleton
        LibraryConfig cfg1 = LibraryConfig.INSTANCE;
        LibraryConfig cfg2 = LibraryConfig.INSTANCE;
        System.out.println("cfg1 == cfg2 (même instance) : " + (cfg1 == cfg2));

        // Modification de la config via l'instance unique
        cfg1.setMaxBooksPerUser(7);
        System.out.println("Après modification via cfg1 : maxBooks=" + cfg2.getMaxBooksPerUser());
        System.out.println("Library config : " + LibraryConfig.INSTANCE);

        System.out.println();

        /*
         * RÉPONSE : Pourquoi l'Enum Singleton est supérieur ?
         *
         * 1. Sérialisation : Java garantit une seule instance même après
         *    sérialisation/désérialisation (le Singleton classique peut en créer une 2e).
         *
         * 2. Réflexion : impossible de créer une instance via Reflection.new Instance()
         *    sur une Enum (le Singleton classique peut être cassé ainsi).
         *
         * 3. Thread-safety native : aucun synchronized nécessaire.
         *
         * 4. Code minimal : pas de boilerplate volatile/synchronized.
         */
    }

    // =====================================================================
    // PATTERN 4 : PROTOTYPE
    // =====================================================================
    private void demoPrototype() {
        System.out.println("--- PATTERN 4 : PROTOTYPE ---");

        // Profil original
        UserPreferences original = new UserPreferences("Fantasy", "fr", 14);
        original.addFavoriteAuthor("Tolkien");
        original.addFavoriteAuthor("Le Guin");
        original.setNotificationPref("email", true);
        original.setNotificationPref("sms", false);

        System.out.println("Original avant clone : " + original);

        // Clonage deep copy
        UserPreferences clone = original.clone();

        // Modification du clone → NE DOIT PAS affecter l'original
        clone.setPreferredGenre("Science-Fiction");
        clone.setLanguage("en");
        clone.addFavoriteAuthor("Asimov");
        clone.setNotificationPref("sms", true);

        System.out.println("Clone modifié  : " + clone);
        System.out.println("Original après : " + original);

        // Vérification que l'original est intact
        boolean originalIntact =
                "Fantasy".equals(original.getPreferredGenre()) &&
                original.getFavoriteAuthors().size() == 2 &&
                Boolean.FALSE.equals(original.getNotificationPrefs().get("sms"));

        System.out.println("Original intact (deep copy) : " + originalIntact);

        System.out.println();

        /*
         * RÉPONSE : Pourquoi une deep copy est importante ?
         *
         * Avec une shallow copy, la liste `favoriteAuthors` du clone pointe
         * vers le MÊME objet List que l'original.
         * Ajouter "Asimov" dans le clone ajouterait aussi "Asimov" dans l'original → bug !
         * La deep copy crée une nouvelle ArrayList indépendante pour le clone,
         * garantissant que les deux profils évoluent indépendamment.
         */
    }
}
