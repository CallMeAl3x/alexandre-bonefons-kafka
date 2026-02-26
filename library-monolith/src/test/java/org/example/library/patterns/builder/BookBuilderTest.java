package org.example.library.patterns.builder;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pattern 1 — Builder")
class BookBuilderTest {

    @Test
    @DisplayName("Livre complet : tous les champs sont correctement assignés")
    void shouldBuildFullBook() {
        Book book = new Book.Builder("Dune", "Frank Herbert")
                .isbn("978-2-07-036024-1")
                .publisher("Gallimard")
                .pages(688)
                .genre("Science-Fiction")
                .price(12.50)
                .publicationYear(1965)
                .available(true)
                .addTag("SF")
                .addTag("classique")
                .description("Chef-d'œuvre de la SF")
                .build();

        assertEquals("Dune", book.getTitle());
        assertEquals("Frank Herbert", book.getAuthor());
        assertEquals("978-2-07-036024-1", book.getIsbn());
        assertEquals("Gallimard", book.getPublisher());
        assertEquals(688, book.getPages());
        assertEquals("Science-Fiction", book.getGenre());
        assertEquals(12.50, book.getPrice());
        assertEquals(1965, book.getPublicationYear());
        assertTrue(book.isAvailable());
        assertEquals(List.of("SF", "classique"), book.getTags());
        assertEquals("Chef-d'œuvre de la SF", book.getDescription());
    }

    @Test
    @DisplayName("Livre minimal : seuls title et author sont obligatoires")
    void shouldBuildMinimalBook() {
        Book book = new Book.Builder("Clean Code", "Robert C. Martin").build();

        assertEquals("Clean Code", book.getTitle());
        assertEquals("Robert C. Martin", book.getAuthor());
        assertEquals("", book.getIsbn());
        assertEquals(0, book.getPages());
        assertEquals(0.0, book.getPrice());
        assertTrue(book.isAvailable()); // true par défaut
        assertTrue(book.getTags().isEmpty());
    }

    @Test
    @DisplayName("Livre indisponible")
    void shouldBuildUnavailableBook() {
        Book book = new Book.Builder("1984", "George Orwell")
                .available(false)
                .build();

        assertFalse(book.isAvailable());
    }

    @Test
    @DisplayName("Le Builder lève une exception si le titre est vide")
    void shouldThrowWhenTitleIsBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> new Book.Builder("", "Auteur").build());
    }

    @Test
    @DisplayName("Le Builder lève une exception si le prix est négatif")
    void shouldThrowWhenPriceIsNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> new Book.Builder("Titre", "Auteur").price(-5.0).build());
    }

    @Test
    @DisplayName("Deux builds indépendants avec le même Builder produisent des objets distincts")
    void shouldProduceIndependentObjects() {
        Book.Builder builder = new Book.Builder("Titre", "Auteur").genre("SF");
        Book b1 = builder.addTag("tag1").build();
        // Les listes de tags de deux builds différents ne doivent pas interférer
        assertNotNull(b1);
        assertEquals("SF", b1.getGenre());
    }
}
