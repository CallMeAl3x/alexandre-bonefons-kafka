package org.example.library.solid.i.solution;

/**
 * ✅ ISP — Interfaces ségrégées.
 *
 * Au lieu d'une grosse interface, on crée des interfaces cohérentes et
 * focalisées. Chaque classe n'implémente que ce dont elle a besoin.
 *
 * RÉPONSE : Les interfaces spécialisées sont meilleures car :
 * - Aucune classe n'est forcée d'implémenter des méthodes vides/exceptions
 * - Le code client dépend uniquement de ce qu'il utilise réellement
 * - Évolution : ajouter une méthode dans DigitalBook n'impacte pas PrintedBook
 */

// Contrat commun à tous les livres
interface Readable {
    String getTitle();
    String getAuthor();
}

// Spécifique aux livres numériques
interface DigitalBook extends Readable {
    void downloadPDF();
    void openEReader();
}

// Spécifique aux livres physiques
interface PhysicalBook extends Readable {
    void printBook();
    void bindBook();
}

// Spécifique à la gestion des emprunts (applicable aux deux)
interface Loanable {
    void registerLoan();
    void calculateFine();
}

// ✅ EBook : numérique + emprunts — N'implémente PAS PhysicalBook
class EBook implements DigitalBook, Loanable {
    private final String title, author;
    public EBook(String title, String author) { this.title = title; this.author = author; }

    @Override public String getTitle()    { return title; }
    @Override public String getAuthor()   { return author; }
    @Override public void downloadPDF()   { System.out.println("[EBook] Downloading PDF: " + title); }
    @Override public void openEReader()   { System.out.println("[EBook] Opening e-reader: " + title); }
    @Override public void registerLoan()  { System.out.println("[EBook] Loan registered: " + title); }
    @Override public void calculateFine() { System.out.println("[EBook] Fine calculated: " + title); }
}

// ✅ PrintedBook : physique + emprunts — N'implémente PAS DigitalBook
class PrintedBook implements PhysicalBook, Loanable {
    private final String title, author;
    public PrintedBook(String title, String author) { this.title = title; this.author = author; }

    @Override public String getTitle()    { return title; }
    @Override public String getAuthor()   { return author; }
    @Override public void printBook()     { System.out.println("[PrintedBook] Printing: " + title); }
    @Override public void bindBook()      { System.out.println("[PrintedBook] Binding: " + title); }
    @Override public void registerLoan()  { System.out.println("[PrintedBook] Loan registered: " + title); }
    @Override public void calculateFine() { System.out.println("[PrintedBook] Fine calculated: " + title); }
}
