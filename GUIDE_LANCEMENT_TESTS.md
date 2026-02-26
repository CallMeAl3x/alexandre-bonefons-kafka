# Guide de lancement & tests — TP Bibliothèque (Monolithe + Microservices)

## 1) Prérequis

- Java 17
- Maven (ou `./mvnw` selon le module)
- Docker + Docker Compose
- Bruno (pour tester les endpoints)

---

## 2) Démarrage rapide (infrastructure)

Depuis la racine du projet :

```bash
docker-compose up -d
```

Cela démarre notamment les dépendances d’infra utilisées par le projet (Kafka / services liés au compose actuel).

---

## 3) Exécution des tests (code)

### Monolithe (`library-monolith`)

```bash
cd library-monolith
mvn test
```

Résultat attendu : build en succès avec les tests Patterns + SOLID.

### Microservices Exo 4 (compilation rapide)

```bash
cd ../library-book-service && mvn compile
cd ../library-user-service && mvn compile
cd ../library-loan-service && mvn compile
```

---

## 4) Lancer l’architecture microservices (Exo 4)

⚠️ Ordre recommandé :

1. `discovery-service` (Eureka)
2. `api-gateway`
3. `library-book-service`
4. `library-user-service`
5. `library-loan-service`

### 4.1 Discovery Service

```bash
cd discovery-service
./mvnw spring-boot:run
```

- URL utile : `http://localhost:8761`

### 4.2 API Gateway

```bash
cd ../api-gateway
./mvnw spring-boot:run
```

- Port : `8090`
- Routes library configurées :
  - `/books/**` → `library-book-service`
  - `/users/**` → `library-user-service`
  - `/library-loans/**` → `library-loan-service`

### 4.3 Book Service

```bash
cd ../library-book-service
mvn spring-boot:run
```

- Port : `8081`

### 4.4 User Service

```bash
cd ../library-user-service
mvn spring-boot:run
```

- Port : `8082`

### 4.5 Loan Service

```bash
cd ../library-loan-service
mvn spring-boot:run
```

- Port : `8083`

---

## 5) Lancer le monolithe (Exo 3)

```bash
cd library-monolith
mvn spring-boot:run
```

- Port : `8095`
- Endpoints principaux : `/books`, `/users`, `/loans`, `/reviews`

---

## 6) Tests API avec Bruno

## 6.1 Collection monolithe

Ouvrir le dossier :

`/Users/bonefonsalexandre/Documents/bruno/library/`

Base URL utilisée dans les requêtes : `http://localhost:8095`

## 6.2 Collection microservices

Ouvrir le dossier :

`/Users/bonefonsalexandre/Documents/bruno/library-microservices/`

Base URL utilisée dans les requêtes : `http://localhost:8090` (via API Gateway)

---

## 7) Parcours de test conseillé (microservices)

1. `GET /books` (vérifier seed data book)
2. `GET /users` (vérifier seed data user)
3. `POST /library-loans` avec `{ "bookId": 2, "userId": 1 }`
4. `GET /library-loans/active`
5. `PUT /library-loans/{id}/return`
6. `GET /books/{bookId}` pour vérifier le retour de disponibilité

---

## 8) Dépannage rapide

### Erreur "Port already in use"

Trouver et tuer le process du port concerné, puis relancer le service.

### 404 sur `/books` ou `/users`

- Vérifier que `api-gateway` est lancé
- Vérifier dans Eureka (`http://localhost:8761`) que les services sont `UP`
- Vérifier que l’appel passe bien par le bon port (`8090` pour microservices, `8095` pour monolithe)

### La collection Bruno n’apparaît pas

Dans Bruno : `+` → `Open Collection` → sélectionner le dossier collection (`library` ou `library-microservices`).

---

# Partie explicative

## A) Vision globale du TP

Le TP est construit en progression :

1. **Exo 1 (Design Patterns)** : apprendre des solutions de conception (Builder, Factory, Singleton, Prototype).
2. **Exo 2 (SOLID)** : améliorer la qualité de design (maintenabilité, extensibilité, découplage).
3. **Exo 3 (Monolithe)** : construire une API complète en architecture en couches (Controller / Service / Repository / DTO / Exception handler).
4. **Exo 4 (Microservices)** : découper le monolithe en services indépendants et les faire communiquer.

## B) Ce qui change entre monolithe et microservices

### Monolithe

- Un seul process, une seule base, appels internes (in-memory)
- Simple à démarrer et debugger
- Plus difficile à scaler et à faire évoluer par domaine

### Microservices

- Plusieurs applications indépendantes (Book/User/Loan)
- Chaque service gère son modèle et sa base
- Communication réseau (REST via Gateway + Discovery)
- Plus flexible, mais plus complexe opérationnellement

## C) Communication inter-services dans l’Exo 4

Le `library-loan-service` ne possède pas les tables `Book` et `User` complètes :

- il stocke seulement `bookId` et `userId`
- il **valide** ces IDs en appelant :
  - `library-book-service` (`GET /books/{id}`)
  - `library-user-service` (`GET /users/{id}`)
- il met à jour la disponibilité du livre via :
  - `PATCH /books/{id}/availability`

Cela matérialise un vrai découplage par domaine.

## D) Avantages / inconvénients (réponse attendue Exo 4)

### Avantages

- Déploiement indépendant par service
- Scalabilité ciblée (ex: Loan à forte charge)
- Isolation des pannes (une panne locale n’arrête pas tout)
- Meilleure ownership par équipe/domaine

### Inconvénients

- Complexité accrue (ops, monitoring, réseau)
- Debug plus difficile (traces distribuées)
- Gestion de cohérence des données plus délicate
- Besoin de résilience (timeouts, retries, fallback)

## E) Pourquoi le Gateway + Eureka sont importants

- **Eureka** : registre dynamique des services (`UP/DOWN`, discovery)
- **Gateway** : point d’entrée unique (routing, sécurité potentielle, cross-cutting)

Sans eux, il faudrait coder les URLs/ports en dur partout, ce qui devient fragile en distribué.

---

## 9) Commandes utiles (résumé)

```bash
# infra
docker-compose up -d

# tests monolithe
cd library-monolith && mvn test

# run discovery
cd discovery-service && ./mvnw spring-boot:run

# run gateway
cd api-gateway && ./mvnw spring-boot:run

# run microservices
cd library-book-service && mvn spring-boot:run
cd library-user-service && mvn spring-boot:run
cd library-loan-service && mvn spring-boot:run
```
