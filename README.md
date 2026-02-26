# TP Architecture Logicielle — Ynov M1

Lien github : https://github.com/CallMeAl3x/alexandre-bonefons-kafka

## Étudiant
- Nom : Alexandre Bonefons
- Sujet : Plateforme de gestion de bibliothèque (Design Patterns, SOLID, Monolithe, Microservices)

## Objectif du rendu
Ce repository contient une progression complète en 4 exercices :
1. Design Patterns
2. Principes SOLID
3. Architecture Monolithique Spring Boot
4. Migration vers Microservices

## Branches de rendu
Chaque exercice est isolé dans sa branche dédiée :
- `exo-1` : uniquement l’exercice 1
- `exo-2` : uniquement l’exercice 2
- `exo-3` : uniquement l’exercice 3
- `exo-4` : uniquement l’exercice 4
- `main` : vue complète (exo 1 à 4)

## Stack technique
- Java 17
- Spring Boot 3.x
- Spring Data JPA
- H2 (in-memory)
- Spring Cloud (Eureka + Gateway + Config Server)
- Maven
- Bruno (tests API)

## Structure du projet
- `library-monolith/` : exo 1, 2, 3
- `library-book-service/` : microservice livres (exo 4)
- `library-user-service/` : microservice utilisateurs (exo 4)
- `library-loan-service/` : microservice emprunts (exo 4)
- `discovery-service/` : Eureka
- `api-gateway/` : point d’entrée unique
- `config-server/` : serveur de configuration
- `tp/TP_ARCHITECTURE_DESIGN_PATTERNS_SOLID.md` : énoncé
- `GUIDE_LANCEMENT_TESTS.md` : guide détaillé d’exécution et de test

## Exécution rapide
Consulter le guide complet :
- `GUIDE_LANCEMENT_TESTS.md`

Résumé :
1. Lancer `discovery-service`
2. Lancer `api-gateway`
3. Lancer `library-book-service`
4. Lancer `library-user-service`
5. Lancer `library-loan-service`
6. Tester via Bruno (gateway `http://localhost:8090`)

Pour le monolithe (exo 3) :
- Lancer `library-monolith` sur `http://localhost:8095`

## Commandes à lancer par exercice

### Exo 1 — Design Patterns (`exo-1`)

```bash
git switch exo-1
cd library-monolith
mvn test
```

### Exo 2 — SOLID (`exo-2`)

```bash
git switch exo-2
cd library-monolith
mvn test
```

### Exo 3 — Monolithe REST (`exo-3`)

```bash
git switch exo-3
cd library-monolith

# lancer l'application
mvn spring-boot:run

# dans un autre terminal (optionnel)
mvn test
```

Base API : `http://localhost:8095`

### Exo 4 — Microservices (`exo-4`)

```bash
git switch exo-4

# terminal 1
cd discovery-service && ./mvnw spring-boot:run

# terminal 2
cd api-gateway && ./mvnw spring-boot:run

# terminal 3
cd library-book-service && mvn spring-boot:run

# terminal 4
cd library-user-service && mvn spring-boot:run

# terminal 5
cd library-loan-service && mvn spring-boot:run
```

Vérification compilation rapide :

```bash
cd library-book-service && mvn compile
cd ../library-user-service && mvn compile
cd ../library-loan-service && mvn compile
```

Base API Gateway : `http://localhost:8090`

## Éléments attendus par exercice
### Exo 1 — Design Patterns
- Builder
- Factory
- Singleton
- Prototype
- Démonstration + tests unitaires

### Exo 2 — SOLID
- SRP, OCP, LSP, ISP, DIP
- Version “problème” + version “solution”
- Démonstration + tests unitaires

### Exo 3 — Monolithe
- Architecture en couches (Controller / Service / Repository / DTO)
- Gestion des exceptions globale
- Seed data (`data.sql`)
- Endpoints REST Books / Users / Loans / Reviews

### Exo 4 — Microservices
- Découpage en services indépendants
- Communication inter-services (REST)
- Service Discovery (Eureka)
- API Gateway
- Tests des flux via Bruno

## Vérification rapide
- Tests Java (monolithe) :
  - `cd library-monolith && mvn test`
- Compilation microservices :
  - `cd library-book-service && mvn compile`
  - `cd library-user-service && mvn compile`
  - `cd library-loan-service && mvn compile`
