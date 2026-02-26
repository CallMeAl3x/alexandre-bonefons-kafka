package org.example.library.patterns.builder;

import java.util.ArrayList;
import java.util.List;

/**
 * BUILDER PATTERN
 *
 * Objectif : Construire des objets complexes avec une API fluide.
 * On évite les constructeurs à N paramètres difficiles à lire et à maintenir.
 *
 * Usage :
 *   Book book = new Book.Builder("Le Seigneur des Anneaux", "Tolkien")
 *       .isbn("978-2-07-070322-8")
 *       .publisher("Gallimard")
 *       .pages(1200)
 *       .genre("Fantasy")
 *       .addTag("epique")
 *       .addTag("aventure")
 *       .available(true)
 *       .build();
 */
public class Book {

    // Attributs obligatoires
    private final String title;
    private final String author;

    // Attributs optionnels
    private final String isbn;
    private final String publisher;
    private final int pages;
    private final String genre;
    private final double price;
    private final int publicationYear;
    private final boolean available;
    private final List<String> tags;
    private final String description;

    // Constructeur privé : seul le Builder peut instancier un Book
    private Book(Builder builder) {
        this.title           = builder.title;
        this.author          = builder.author;
        this.isbn            = builder.isbn;
        this.publisher       = builder.publisher;
        this.pages           = builder.pages;
        this.genre           = builder.genre;
        this.price           = builder.price;
        this.publicationYear = builder.publicationYear;
        this.available       = builder.available;
        this.tags            = builder.tags;
        this.description     = builder.description;
    }

    // --- Getters ---

    public String getTitle()           { return title; }
    public String getAuthor()          { return author; }
    public String getIsbn()            { return isbn; }
    public String getPublisher()       { return publisher; }
    public int    getPages()           { return pages; }
    public String getGenre()           { return genre; }
    public double getPrice()           { return price; }
    public int    getPublicationYear() { return publicationYear; }
    public boolean isAvailable()       { return available; }
    public List<String> getTags()      { return tags; }
    public String getDescription()     { return description; }

    @Override
    public String toString() {
        return "Book{" +
                "title='"          + title          + '\'' +
                ", author='"       + author         + '\'' +
                ", isbn='"         + isbn           + '\'' +
                ", publisher='"    + publisher      + '\'' +
                ", pages="         + pages          +
                ", genre='"        + genre          + '\'' +
                ", price="         + price          +
                ", year="          + publicationYear +
                ", available="     + available      +
                ", tags="          + tags           +
                ", description='"  + description    + '\'' +
                '}';
    }

    // =====================================================================
    // BUILDER STATIQUE IMBRIQUÉ
    // =====================================================================
    public static class Builder {

        // Attributs obligatoires
        private final String title;
        private final String author;

        // Attributs optionnels avec valeurs par défaut
        private String isbn            = "";
        private String publisher       = "";
        private int    pages           = 0;
        private String genre           = "";
        private double price           = 0.0;
        private int    publicationYear = 0;
        private boolean available      = true;
        private List<String> tags      = new ArrayList<>();
        private String description     = "";

        /**
         * Seuls title et author sont obligatoires.
         */
        public Builder(String title, String author) {
            if (title == null || title.isBlank()) {
                throw new IllegalArgumentException("Le titre est obligatoire");
            }
            if (author == null || author.isBlank()) {
                throw new IllegalArgumentException("L'auteur est obligatoire");
            }
            this.title  = title;
            this.author = author;
        }

        public Builder isbn(String isbn) {
            this.isbn = isbn;
            return this;
        }

        public Builder publisher(String publisher) {
            this.publisher = publisher;
            return this;
        }

        public Builder pages(int pages) {
            if (pages < 0) throw new IllegalArgumentException("Le nombre de pages ne peut pas être négatif");
            this.pages = pages;
            return this;
        }

        public Builder genre(String genre) {
            this.genre = genre;
            return this;
        }

        public Builder price(double price) {
            if (price < 0) throw new IllegalArgumentException("Le prix ne peut pas être négatif");
            this.price = price;
            return this;
        }

        public Builder publicationYear(int year) {
            this.publicationYear = year;
            return this;
        }

        public Builder available(boolean available) {
            this.available = available;
            return this;
        }

        public Builder addTag(String tag) {
            this.tags.add(tag);
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        /**
         * Construit et retourne l'objet Book immuable.
         */
        public Book build() {
            return new Book(this);
        }
    }
}
