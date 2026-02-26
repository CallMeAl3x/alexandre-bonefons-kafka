# TP COMPLET - Architecture Logicielle, Design Patterns & SOLID
## Ynov M1 - Implémentation d'une Plateforme de Gestion de Bibliothèque Distribuée

---

## Table des Matières
1. [Introduction & Objectifs](#introduction)
2. [Architecture Générale](#architecture)
3. [Exercice 1 : Design Patterns Fondamentaux](#exercice-1)
4. [Exercice 2 : SOLID Principles](#exercice-2)
5. [Exercice 3 : Architecture Monolithique](#exercice-3)
6. [Exercice 4 : Migration vers Microservices](#exercice-4)
7. [Bonus : Patterns Avancés](#bonus)
8. [Barème & Critères d'Évaluation](#barème)

---

## Introduction & Objectifs {#introduction}

### Objectifs Pédagogiques

À la fin de ce TP, vous serez capable de :
- Implémenter les 6 design patterns fondamentaux
- Appliquer les 5 principes SOLID correctement
- Concevoir une architecture en couches avec Spring Boot
- Migrer une application monolithique vers une architecture microservices
- Communiquer entre services via REST
- Gérer les erreurs et exceptions globalement

### Contexte du TP

Vous devez construire une plateforme de gestion de bibliothèque distribuée supportant :
- Gestion de livres (ajout, modification, recherche)
- Gestion de lecteurs (inscriptions, historique)
- Gestion des emprunts (création, retour, amendes)
- Système d'avis et critiques
- Recherche et recommandations

### Stack Technique Requis

```
Language: Java 17+
Framework: Spring Boot 3.3.4
ORM: Spring Data JPA
Database: H2 (in-memory)
Build: Maven
Additional: Lombok, RestTemplate
Testing: JUnit 5
```

---

## Architecture Générale {#architecture}

### Vue d'ensemble

```
┌──────────────────────────────────────────────────────────┐
│             Plateforme Bibliothèque                      │
└──────────────────────────────────────────────────────────┘
                              │
                ┌─────────────┼─────────────┐
                │             │             │
        ┌───────▼────────┐ ┌─┴──────────┐ ┌┴─────────────┐
        │ API Gateway    │ │ Discovery  │ │ Config Srv   │
        │ (Port 8080)    │ │ Service    │ │ (Optional)   │
        └───────┬────────┘ └────────────┘ └──────────────┘
                │
    ┌───────────┼───────────┬───────────┐
    │           │           │           │
┌───▼────┐ ┌───▼────┐ ┌───▼────┐ ┌───▼────┐
│ Book   │ │ User   │ │ Loan   │ │ Review │
│Service │ │Service │ │Service │ │Service │
│ :8081  │ │ :8082  │ │ :8083  │ │ :8084  │
└───┬────┘ └───┬────┘ └───┬────┘ └───┬────┘
    │ H2   │ H2   │ H2   │ H2
    └──────┴──────┴──────┴──────
         │       │       │       │
        DB1     DB2     DB3     DB4
```

### Communication Entre Services

```
Loan Service (8083)
        ├─ Call Book Service (8081) → Validate Book ID
        ├─ Call User Service (8082) → Check User Membership
        └─ Call Review Service (8084) → Fetch Reviews

Review Service (8084)
        ├─ Call Book Service (8081) → Get Book Details
        └─ Call User Service (8082) → Get User Info
```

---

## Exercice 1 : Design Patterns Fondamentaux {#exercice-1}

### Objectif
Implémenter les 6 design patterns clés dans un contexte de gestion de bibliothèque.

---

### Pattern 1 : BUILDER PATTERN

#### Objectif
Construire des objets complexes avec une API fluide.

#### Contexte
Création de livres avec de nombreux attributs optionnels (auteur, éditeur, ISBN, tags, etc.)


#### A faire

- [ ] Créer la classe Book avec la structure du Builder
- [ ] Implémenter tous les setters du Builder
- [ ] Tester avec différentes combinaisons d'attributs
- [ ] Question: Quel est l'avantage du Builder par rapport à un constructeur avec 10 paramètres ?

---

### Pattern 2 : FACTORY PATTERN

#### Objectif
Créer des objets sans spécifier leurs classes concrètes.

#### Contexte
Création de services de notification différents (Email, SMS, Push) sans que le client connaisse les classes concrètes.

#### Approche A: Multiple Factory Classes

#### Approche B: Simple Factory Pattern

#### A faire

- [ ] Créer l'interface NotificationService avec 3 implémentations
- [ ] Implémenter la version Multiple Factory Classes
- [ ] Implémenter la version Simple Factory Pattern
- [ ] Créer la classe de démonstration
- [ ] Question: Quelle approche est plus flexible pour ajouter un nouveau type de notification ?

---

### Pattern 3 : SINGLETON PATTERN

#### Objectif
Assurer qu'une classe n'a qu'une seule instance.

#### Contexte
Configuration de la bibliothèque (base de données, connexions) - doit être unique dans l'application.


#### A faire

- [ ] Implémenter le DatabaseConfig avec lazy initialization
- [ ] Implémenter l'Enum Singleton LibraryConfig
- [ ] Tester que les deux approches retournent la même instance
- [ ] Question: Pourquoi l'Enum Singleton est-il supérieur au Singleton classique ?

---

### Pattern 4 : PROTOTYPE PATTERN

#### Objectif
Cloner des objets sans connaître leurs types exacts.

#### Contexte
Copier les configurations de lecture d'un lecteur existant pour créer un nouveau profil.


#### A faire

- [ ] Créer la classe UserPreferences avec Cloneable
- [ ] Implémenter une deep copy dans la méthode clone()
- [ ] Tester que le clonage n'affecte pas l'original
- [ ] Question: Pourquoi une deep copy est-elle importante ici ?

---

## Exercice 2 : SOLID Principles {#exercice-2}

### Objectif
Refactoriser du code problématique pour respecter les 5 principes SOLID.

---

### PRINCIPLE 1 : SINGLE RESPONSIBILITY PRINCIPLE (SRP)

#### Problème

**File: src/main/java/org/example/solid/s/problem/UserManager.java**
```java
package org.example.solid.s.problem;

import java.util.*;

public class UserManager {
    private List<String> users = new ArrayList<>();

    public void addUser(String username) {
        if (isValidUsername(username)) {
            users.add(username);
            System.out.println("User added: " + username);
        }
    }

    public void removeUser(String username) {
        users.remove(username);
    }

    public boolean isValidUsername(String username) {
        return username != null && username.length() >= 3;
    }

    public boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }

    public void notifyUser(String username, String message) {
        System.out.println("Email sent to " + username + ": " + message);
    }

    public void saveUserToDatabase(String username) {
        System.out.println("Saving " + username + " to database");
    }
}
```

Problèmes:
- Trop de responsabilités (4 différentes)
- Difficile à tester
- Difficile à modifier
- Réutilisabilité faible

#### A faire

- [ ] Analyser la classe UserManager problématique
- [ ] Implémenter les 4 classes séparées avec une responsabilité chacune
- [ ] Créer le service qui orchestre ces 4 classes
- [ ] Tester l'enregistrement d'un utilisateur
- [ ] Question: Pourquoi c'est plus facile de tester maintenant ?

---

### PRINCIPLE 2 : OPEN/CLOSED PRINCIPLE (OCP)

#### Problème

**File: src/main/java/org/example/solid/o/problem/BookRater.java**
```java
package org.example.solid.o.problem;

public class BookRater {

    public double calculateRating(String bookType, int reviewCount) {
        switch(bookType) {
            case "fiction":
                return calculateFictionRating(reviewCount);
            case "science":
                return calculateScienceRating(reviewCount);
            case "history":
                return calculateHistoryRating(reviewCount);
            default:
                return 0;
        }
    }

    private double calculateFictionRating(int reviewCount) {
        return Math.min(5, reviewCount * 0.1 + 2.0);
    }

    private double calculateScienceRating(int reviewCount) {
        return Math.min(5, reviewCount * 0.15 + 2.5);
    }

    private double calculateHistoryRating(int reviewCount) {
        return Math.min(5, reviewCount * 0.08 + 3.0);
    }
}
```

Problèmes:
- Fermée pour l'extension (switch case)
- Ouverte pour la modification (ajouter un type = modifier la classe)
- Violation du OCP


#### A faire

- [ ] Identifier le problème dans la classe BookRater
- [ ] Créer l'interface RatingStrategy avec 3 implémentations
- [ ] Refactoriser BookRater avec une Map de stratégies
- [ ] Tester l'ajout d'un nouveau type sans modification
- [ ] Question: Comment les stratégies rendent la classe ouverte/fermée ?

---

### PRINCIPLE 3 : LISKOV SUBSTITUTION PRINCIPLE (LSP)

#### Problème

**File: src/main/java/org/example/solid/l/problem/Bird.java**
```java
package org.example.solid.l.problem;

public abstract class Bird {
    public abstract void fly();
    public abstract void eat();
}

public class Sparrow extends Bird {
    @Override
    public void fly() {
        System.out.println("Sparrow is flying");
    }

    @Override
    public void eat() {
        System.out.println("Sparrow is eating seeds");
    }
}

public class Ostrich extends Bird {
    @Override
    public void fly() {
        throw new UnsupportedOperationException("Ostrich cannot fly!");
    }

    @Override
    public void eat() {
        System.out.println("Ostrich is eating plants");
    }
}
```

Problèmes:
- Ostrich.fly() viole le contrat Bird
- Pas substituable
- Code client doit connaître les détails d'implémentation

#### A faire

- [ ] Identifier la violation LSP avec Ostrich.fly()
- [ ] Créer une hiérarchie d'interfaces appropriée
- [ ] Implémenter les oiseaux avec les bons contrats
- [ ] Démontrer la substitutabilité
- [ ] Question: Comment les interfaces résolvent le problème ?

---

### PRINCIPLE 4 : INTERFACE SEGREGATION PRINCIPLE (ISP)

#### Problème

**File: src/main/java/org/example/solid/i/problem/Book.java**
```java
package org.example.solid.i.problem;

public interface Book {
    String getTitle();
    String getAuthor();

    void downloadPDF();
    void openEReader();

    void printBook();
    void bindBook();

    void registerLoan();
    void calculateFine();
}

public class EBook implements Book {
    @Override
    public void downloadPDF() {
        System.out.println("Downloading PDF...");
    }

    @Override
    public void printBook() {
        throw new UnsupportedOperationException("Cannot print eBook!");
    }
}
```

Problèmes:
- Interface trop grosse (Fat Interface)
- Classes forcées d'implémenter des méthodes inutiles
- Couplage fort

#### A faire

- [ ] Analyser l'interface trop grosse Book
- [ ] Créer des interfaces ségrégées
- [ ] Implémenter PrintedBook et EBook
- [ ] Tester que chaque classe n'implémente que les méthodes nécessaires
- [ ] Question: Pourquoi les interfaces spécialisées sont-elles meilleures ?

---

### PRINCIPLE 5 : DEPENDENCY INVERSION PRINCIPLE (DIP)

#### Problème

**File: src/main/java/org/example/solid/d/problem/OrderService.java**
```java
package org.example.solid.d.problem;

public class OrderService {
    private EmailNotification emailService;
    private DatabaseRepository dbRepo;

    public OrderService() {
        this.emailService = new EmailNotification();
        this.dbRepo = new DatabaseRepository();
    }

    public void createOrder(String userId, String bookId) {
        dbRepo.save("order_" + userId, bookId);
        emailService.sendEmail(userId, "Order created successfully");
    }
}

public class EmailNotification {
    public void sendEmail(String user, String message) {
        System.out.println("Email to " + user + ": " + message);
    }
}

public class DatabaseRepository {
    public void save(String key, String value) {
        System.out.println("Saving to DB: " + key + " = " + value);
    }
}
```

Problèmes:
- Dépend de classes concrètes
- Couplage fort
- Difficile à tester (pas de mock)


#### A faire

- [ ] Créer les interfaces NotificationService et DataRepository
- [ ] Implémenter 2 versions pour chaque interface
- [ ] Refactoriser OrderService avec injection de dépendances
- [ ] Tester avec différentes combinaisons
- [ ] Question: Comment l'injection facilite-t-elle les tests ?

---

## Exercice 3 : Architecture Monolithique {#exercice-3}

### Objectif
Construire une application monolithique complète en couches avec Spring Boot.

### Structure du Projet

```
library-monolith/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/org/example/
│   │   │   ├── LibraryApplication.java
│   │   │   ├── config/
│   │   │   │   ├── AppConfig.java
│   │   │   │   └── JpaConfig.java
│   │   │   ├── controller/
│   │   │   │   ├── BookController.java
│   │   │   │   ├── UserController.java
│   │   │   │   ├── LoanController.java
│   │   │   │   └── ReviewController.java
│   │   │   ├── service/
│   │   │   │   ├── BookService.java (interface)
│   │   │   │   ├── BookServiceImpl.java
│   │   │   │   ├── UserService.java (interface)
│   │   │   │   ├── UserServiceImpl.java
│   │   │   │   ├── LoanService.java (interface)
│   │   │   │   ├── LoanServiceImpl.java
│   │   │   │   ├── ReviewService.java (interface)
│   │   │   │   └── ReviewServiceImpl.java
│   │   │   ├── model/
│   │   │   │   ├── Book.java
│   │   │   │   ├── User.java
│   │   │   │   ├── Loan.java
│   │   │   │   └── Review.java
│   │   │   ├── dto/
│   │   │   │   ├── BookDTO.java
│   │   │   │   ├── UserDTO.java
│   │   │   │   ├── LoanDTO.java
│   │   │   │   └── ReviewDTO.java
│   │   │   ├── repository/
│   │   │   │   ├── BookRepository.java
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── LoanRepository.java
│   │   │   │   └── ReviewRepository.java
│   │   │   ├── exception/
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   ├── InvalidBookException.java
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   └── util/
│   │   │       └── DTOMapper.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── data.sql
│   └── test/
│       ├── java/org/example/
│       └── resources/
└── target/
```

### A faire pour l'Exercice 3

- [ ] Créer la structure Maven du projet
- [ ] Implémenter toutes les entities (Book, User, Loan, Review)
- [ ] Créer les DTOs correspondants
- [ ] Créer les repositories JPA
- [ ] Implémenter les services (interfaces + implémentations)
- [ ] Créer les contrôleurs REST
- [ ] Implémenter la gestion globale des exceptions
- [ ] Tester avec Postman ou Curl
- [ ] Question: Pourquoi la séparation en couches est-elle importante ?

---

## Exercice 4 : Migration vers Microservices {#exercice-4}

### Objectif
Décomposer l'application monolithique en microservices indépendants.

### Architecture Microservices

```
Microservices Architecture:

              ┌──────────────────┐
              │  API Gateway     │
              │   (Port 8080)    │
              └────────┬─────────┘
                       │
        ┌──────────────┼──────────────┐
        │              │              │
        ▼              ▼              ▼
    ┌────────┐   ┌────────┐   ┌────────┐
    │ Book   │   │ User   │   │ Loan   │
    │Service │   │Service │   │Service │
    │:8081   │   │:8082   │   │:8083   │
    └────────┘   └────────┘   └────────┘
```

### A faire pour l'Exercice 4

- [ ] Créer la structure Maven pour chaque service
- [ ] Extraire les entités pertinentes dans chaque service
- [ ] Implémenter les clients de communication inter-services
- [ ] Créer les endpoints REST pour chaque service
- [ ] Configurer l'API Gateway
- [ ] Tester les appels inter-services
- [ ] Tester via l'API Gateway
- [ ] Question: Quels sont les avantages et inconvénients des microservices ?

---

## Bonus : Patterns Avancés {#bonus}

### Bonus 1 : Observer Pattern pour les Événements

### Bonus 2 : Strategy Pattern pour les Calculs d'Amendes

### Bonus 3 : Command Pattern pour les Opérations Asynchrones

