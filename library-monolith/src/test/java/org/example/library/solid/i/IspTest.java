package org.example.library.solid.i.solution;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SOLID I — Interface Segregation Principle")
class IspTest {

    @Test @DisplayName("EBook implémente DigitalBook et Loanable")
    void ebookImplementsCorrectInterfaces() {
        EBook ebook = new EBook("Clean Code", "Robert C. Martin");
        assertInstanceOf(DigitalBook.class, ebook);
        assertInstanceOf(Loanable.class,    ebook);
        assertInstanceOf(Readable.class,    ebook);
    }

    @Test @DisplayName("EBook N'implémente PAS PhysicalBook")
    void ebookDoesNotImplementPhysicalBook() {
        EBook ebook = new EBook("Clean Code", "Robert C. Martin");
        assertFalse(ebook instanceof PhysicalBook,
                "EBook ne doit pas être un PhysicalBook");
    }

    @Test @DisplayName("PrintedBook implémente PhysicalBook et Loanable")
    void printedBookImplementsCorrectInterfaces() {
        PrintedBook printed = new PrintedBook("Dune", "Frank Herbert");
        assertInstanceOf(PhysicalBook.class, printed);
        assertInstanceOf(Loanable.class,     printed);
        assertInstanceOf(Readable.class,     printed);
    }

    @Test @DisplayName("PrintedBook N'implémente PAS DigitalBook")
    void printedBookDoesNotImplementDigitalBook() {
        PrintedBook printed = new PrintedBook("Dune", "Frank Herbert");
        assertFalse(printed instanceof DigitalBook,
                "PrintedBook ne doit pas être un DigitalBook");
    }

    @Test @DisplayName("EBook : downloadPDF et openEReader sans exception")
    void ebookDigitalOperations() {
        EBook ebook = new EBook("1984", "Orwell");
        assertDoesNotThrow(ebook::downloadPDF);
        assertDoesNotThrow(ebook::openEReader);
    }

    @Test @DisplayName("PrintedBook : printBook et bindBook sans exception")
    void printedBookPhysicalOperations() {
        PrintedBook printed = new PrintedBook("Dune", "Frank Herbert");
        assertDoesNotThrow(printed::printBook);
        assertDoesNotThrow(printed::bindBook);
    }

    @Test @DisplayName("Les deux types gèrent registerLoan via interface Loanable")
    void bothTypesLoanable() {
        Loanable[] items = {
            new EBook("Book A", "Author A"),
            new PrintedBook("Book B", "Author B")
        };
        for (Loanable item : items) {
            assertDoesNotThrow(item::registerLoan);
            assertDoesNotThrow(item::calculateFine);
        }
    }
}
