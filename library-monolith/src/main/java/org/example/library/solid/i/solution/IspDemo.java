package org.example.library.solid.i.solution;

/**
 * Point d'entrée public pour démontrer l'ISP depuis l'extérieur du package.
 */
public class IspDemo {

    public static void run() {
        System.out.println("\n--- PRINCIPLE 4 : ISP ---");
        System.out.println("\n[❌ Problème] EBook implémente printBook() → UnsupportedOperationException");

        System.out.println("\n[✅ Solution] Interfaces ségrégées");

        EBook ebook = new EBook("Clean Code", "Robert C. Martin");
        PrintedBook printed = new PrintedBook("Le Seigneur des Anneaux", "Tolkien");

        System.out.println("\nEBook (DigitalBook + Loanable) :");
        ebook.downloadPDF();
        ebook.openEReader();
        ebook.registerLoan();
        ebook.calculateFine();
        // ebook.printBook() → n'existe pas → erreur de compilation (pas d'exception à runtime)

        System.out.println("\nPrintedBook (PhysicalBook + Loanable) :");
        printed.printBook();
        printed.bindBook();
        printed.registerLoan();
        printed.calculateFine();
        // printed.downloadPDF() → n'existe pas → erreur de compilation

        System.out.println("\nUtilisation via interface Loanable (les deux types) :");
        processLoan(ebook);
        processLoan(printed);

        System.out.println("\n→ RÉPONSE : Les interfaces spécialisées sont meilleures car " +
                "aucune classe n'est forcée d'implémenter des méthodes inutiles. " +
                "Les erreurs sont détectées à la compilation, pas au runtime.");
    }

    private static void processLoan(Loanable loanable) {
        System.out.println("  Processing loan...");
        loanable.registerLoan();
    }
}
