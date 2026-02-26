package org.example.library.solid.i.problem;

/**
 * ❌ PROBLÈME ISP : Interface Book trop grosse (Fat Interface).
 * EBook est forcé d'implémenter printBook() et bindBook() qui n'ont
 * aucun sens pour lui → UnsupportedOperationException → violation ISP.
 */
public interface Book {
    String getTitle();
    String getAuthor();

    void downloadPDF();    // Numérique seulement
    void openEReader();    // Numérique seulement

    void printBook();      // Physique seulement
    void bindBook();       // Physique seulement

    void registerLoan();   // Gestion bibliothèque
    void calculateFine();  // Gestion bibliothèque
}

class EBook implements Book {
    private String title, author;

    public EBook(String title, String author) {
        this.title = title; this.author = author;
    }

    @Override public String getTitle()  { return title; }
    @Override public String getAuthor() { return author; }
    @Override public void downloadPDF() { System.out.println("Downloading PDF..."); }
    @Override public void openEReader() { System.out.println("Opening e-reader..."); }
    @Override public void registerLoan()  { System.out.println("Loan registered"); }
    @Override public void calculateFine() { System.out.println("Fine calculated"); }

    // ❌ Méthodes sans sens pour un livre numérique
    @Override public void printBook() { throw new UnsupportedOperationException("Cannot print eBook!"); }
    @Override public void bindBook()  { throw new UnsupportedOperationException("Cannot bind eBook!"); }
}
