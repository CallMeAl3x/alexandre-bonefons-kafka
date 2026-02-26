-- Books
INSERT INTO books (title, author, isbn, genre, pages, price, available) VALUES
  ('Dune', 'Frank Herbert', '978-2-07-036024-1', 'Science-Fiction', 688, 12.50, true),
  ('Le Seigneur des Anneaux', 'J.R.R. Tolkien', '978-2-07-070322-8', 'Fantasy', 1200, 24.90, true),
  ('Clean Code', 'Robert C. Martin', '978-0-13-235088-4', 'Informatique', 431, 39.99, true),
  ('1984', 'George Orwell', '978-0-45-228423-4', 'Fiction', 328, 9.90, true),
  ('Fondation', 'Isaac Asimov', '978-2-07-040579-5', 'Science-Fiction', 256, 8.50, true);

-- Users
INSERT INTO users (username, email, first_name, last_name, membership_date) VALUES
  ('alice', 'alice@library.com', 'Alice', 'Martin', '2024-01-15'),
  ('bob', 'bob@library.com', 'Bob', 'Dupont', '2024-03-01'),
  ('charlie', 'charlie@library.com', 'Charlie', 'Bernard', '2025-09-10');
