# TP Architecture Microservice Kafka - Explication

## Objectif du TP

Mettre en place un système de **compteurs en temps réel** pour suivre le nombre de Cards et Loans par Account, en utilisant **Kafka** pour la communication asynchrone entre microservices.

---

## Architecture Globale

```
┌─────────────────┐                     ┌─────────────────┐
│  card-service   │                     │  loans-service  │
│                 │                     │                 │
│  CardController │                     │ LoanController  │
│       │         │                     │       │         │
│       ▼         │                     │       ▼         │
│  CardServiceImpl│                    │ LoanServiceImpl │
│       │         │                     │       │         │
│       ▼         │                     │       ▼         │
│CardKafkaProducer│                     │LoanKafkaProducer│
│  (Producer)     │                     │  (Producer)     │
└───────┬─────────┘                     └───────┬─────────┘
        │                                       │
        │ CARD_CREATED/DELETED                  │ LOAN_CREATED/DELETED
        │                                       │
        └───────────────┬───────────────────────┘
                        │
                        ▼
                ┌──────────────┐
                │    KAFKA      │
                │    BROKER     │
                └───────┬───────┘
                        │
        ┌───────────────┴───────────────────────┐
        │                                       │
        ▼                                       ▼
┌───────────────┐                     ┌───────────────┐
│ card-events   │                     │ loan-events   │
│   topic       │                     │   topic       │
└───────┬───────┘                     └───────┬───────┘
        │                                       │
        └───────────────┬───────────────────────┘
                        ▼
                ┌───────────────┐
                │account-service│
                │               │
                │  Account      │
                │KafkaConsumer  │◄─── (Consumer)
                │               │
                │   Account     │
                │   Entity      │
                │  - totalCards │
                │  - totalLoans │
                └───────────────┘
```

---

## Pourquoi utiliser Kafka ?

### ❌ Approche synchrone (REST)

```java
// Dans account-service
public AccountDTO getAccountWithDetails(Long id) {
    Account account = accountRepository.findById(id);
    // Appel REST bloquant
    List<Card> cards = cardClient.getCardsByAccountId(id);
    List<Loan> loans = loanClient.getLoansByAccountId(id);
    return new AccountDTO(account, cards.size(), loans.size());
}
```

**Problèmes :**
- ⏱️ **Lenteur** : Chaque requête attend les réponses des autres services
- 🔗 **Dépendance forte** : Si card-service est down, account-service ne répond pas
- 🔁 **Requêtes SQL** : COUNT exécuté à chaque appel
- ⏳ **Pas temps réel** : Compteurs mis à jour seulement lors d'une requête

### ✅ Approche asynchrone (Kafka)

```java
// Dans account-service - Entity Account
public class Account {
    private Long id;
    private String name;
    private Long totalCards;   // Stocké, mis à jour en temps réel
    private Long totalLoans;    // Stocké, mis à jour en temps réel
}
```

**Avantages :**
- ⚡ **Performance** : Aucun appel réseau, données déjà disponibles
- 🔓 **Autonomie** : Fonctionne même si les autres services sont down
- 💾 **Optimisé** : Incrément simple (+1/-1) au lieu de COUNT
- 🔄 **Temps réel** : Mise à jour immédiate lors de la création/suppression

---

## Flux des Événements

### Scénario 1 : Création d'une Card

```
1. Utilisateur → POST /cards avec accountId=1
2. card-service → Crée la Card en base
3. card-service → Envoie événement Kafka : {"event":"CARD_CREATED","accountId":"1"}
4. account-service → Reçoit l'événement via AccountKafkaConsumer
5. account-service → Met à jour : account.setTotalCards(account.getTotalCards() + 1)
6. account-service → Sauvegarde en base
```

### Scénario 2 : Suppression d'une Card

```
1. Utilisateur → DELETE /cards/1
2. card-service → Récupère l'accountId avant suppression
3. card-service → Supprime la Card
4. card-service → Envoie événement Kafka : {"event":"CARD_DELETED","accountId":"1"}
5. account-service → Met à jour : account.setTotalCards(account.getTotalCards() - 1)
6. account-service → Sauvegarde en base
```

### Scénario 3 : Suppression d'un Account (Cascade)

```
1. Utilisateur → DELETE /accounts/1
2. account-service → Envoie : {"event":"ACCOUNT_DELETED","accountId":"1"}
3. card-service → Reçoit l'événement → Supprime toutes les Cards de l'Account
4. loans-service → Reçoit l'événement → Supprime tous les Loans de l'Account
```

---

## Modifications Implémentées

### 1. ✅ account-service - Entity Account

**Fichier :** `account-service/src/main/java/org/example/accountservice/entity/Account.java`

**Ajout des champs compteurs :**
```java
@Entity
public class Account {
    private Long id;
    private String name;
    private String email;
    private Integer solde;

    // NOUVEAUX CHAMPS
    private Long totalCards;
    private Long totalLoans;

    // Getters/Setters générés par Lombok @Data
}
```

---

### 2. ✅ account-service - AccountKafkaConsumer

**Fichier :** `account-service/src/main/java/org/example/accountservice/kafka/AccountKafkaConsumer.java`

**Rôle :** Écoute les événements Cards et Loans, met à jour les compteurs

```java
@Service
public class AccountKafkaConsumer {

    @Autowired
    private AccountRepository accountRepository;

    // Écoute les événements des Cards
    @KafkaListener(topics = "card-events", groupId = "account-group")
    public void consumeCardEvent(String message) {
        // Parse le JSON
        JsonNode event = mapper.readTree(message);
        String eventType = event.get("event").asText();
        Long accountId = event.get("accountId").asLong();

        Account account = accountRepository.findById(accountId).orElse(null);

        if (account != null) {
            if ("CARD_CREATED".equals(eventType)) {
                account.setTotalCards(account.getTotalCards() + 1);
            } else if ("CARD_DELETED".equals(eventType)) {
                account.setTotalCards(account.getTotalCards() - 1);
            }
            accountRepository.save(account);
        }
    }

    // Écoute les événements des Loans
    @KafkaListener(topics = "loan-events", groupId = "account-group")
    public void consumeLoanEvent(String message) {
        // Même logique pour les loans
    }
}
```

---

### 3. ✅ card-service - CardKafkaProducer

**Fichier :** `card-service/src/main/java/org/example/cardservice/kafka/CardKafkaProducer.java`

**Rôle :** Envoie des événements Kafka lors de la création/suppression de Cards

```java
@Service
public class CardKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.topic.cards:card-events}")
    private String topic;

    public void sendCardCreated(Long accountId) {
        String event = String.format(
            "{\"event\":\"CARD_CREATED\",\"accountId\":\"%s\"}",
            accountId
        );
        kafkaTemplate.send(topic, event);
    }

    public void sendCardDeleted(Long accountId) {
        String event = String.format(
            "{\"event\":\"CARD_DELETED\",\"accountId\":\"%s\"}",
            accountId
        );
        kafkaTemplate.send(topic, event);
    }
}
```

---

### 4. ✅ card-service - CardServiceImpl

**Fichier :** `card-service/src/main/java/org/example/cardservice/service/impl/CardServiceImpl.java`

**Modification :** Appel du producer après création/suppression

```java
@Service
public class CardServiceImpl {

    @Autowired
    private CardKafkaProducer cardKafkaProducer;

    public Card saveCard(Card card) {
        if (accountServiceClient.accountExists(card.getAccountId())) {
            Card savedCard = cardRepository.save(card);
            // ENVOI ÉVÉNEMENT KAFKA
            cardKafkaProducer.sendCardCreated(card.getAccountId());
            return savedCard;
        }
        throw new IllegalArgumentException("Account does not exist");
    }

    public void deleteCard(Long id) {
        Card card = cardRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Card not found"));
        cardRepository.deleteById(id);
        // ENVOI ÉVÉNEMENT KAFKA
        cardKafkaProducer.sendCardDeleted(card.getAccountId());
    }
}
```

---

### 5. ✅ loans-service - LoanKafkaProducer

**Fichier :** `loans-service/src/main/java/org/example/loansservice/kafka/LoanKafkaProducer.java`

**Rôle :** Envoie des événements Kafka lors de la création/suppression de Loans

```java
@Service
public class LoanKafkaProducer {

    @Value("${spring.kafka.topic.loans:loan-events}")
    private String topic;

    public void sendLoanCreated(Long accountId) {
        String event = String.format(
            "{\"event\":\"LOAN_CREATED\",\"accountId\":\"%s\"}",
            accountId
        );
        kafkaTemplate.send(topic, event);
    }

    public void sendLoanDeleted(Long accountId) {
        String event = String.format(
            "{\"event\":\"LOAN_DELETED\",\"accountId\":\"%s\"}",
            accountId
        );
        kafkaTemplate.send(topic, event);
    }
}
```

---

### 6. ✅ loans-service - LoanServiceImpl

**Fichier :** `loans-service/src/main/java/org/example/loansservice/service/impl/LoanServiceImpl.java`

**Modification :** Appel du producer + ajout de `deleteLoanByAccountId()`

```java
@Service
public class LoanServiceImpl {

    @Autowired
    private LoanKafkaProducer loanKafkaProducer;

    public Loan saveLoan(Loan loan) {
        if (accountServiceClient.accountExists(loan.getAccountId())) {
            Loan savedLoan = loanRepository.save(loan);
            loanKafkaProducer.sendLoanCreated(loan.getAccountId());
            return savedLoan;
        }
        throw new RuntimeException("Account does not exist");
    }

    public void deleteLoan(Long id) {
        Loan loan = loanRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Loan not found"));
        loanRepository.deleteById(id);
        loanKafkaProducer.sendLoanDeleted(loan.getAccountId());
    }

    public void deleteLoanByAccountId(Long accountId) {
        loanRepository.deleteByAccountId(accountId);
    }
}
```

---

### 7. ✅ loans-service - LoanKafkaConsumer

**Fichier :** `loans-service/src/main/java/org/example/loansservice/kafka/LoanKafkaConsumer.java`

**Rôle :** Écoute les événements de suppression d'Account et supprime les Loans associés

```java
@Service
public class LoanKafkaConsumer {

    @Autowired
    private LoanService loanService;

    @KafkaListener(topics = "account-events", groupId = "loan-group")
    public void consumeAccountDeletedEvent(String message) {
        JsonNode event = mapper.readTree(message);

        if ("ACCOUNT_DELETED".equals(event.get("event").asText())) {
            Long accountId = event.get("accountId").asLong();
            loanService.deleteLoanByAccountId(accountId);
        }
    }
}
```

---

## Résumé des Fichiers Modifiés/Créés

| Service | Fichier | Action |
|---------|---------|--------|
| **account-service** | `entity/Account.java` | ➕ Ajout `totalCards`, `totalLoans` |
| **account-service** | `kafka/AccountKafkaConsumer.java` | ➕ Créé - écoute `card-events`, `loan-events` |
| **card-service** | `kafka/CardKafkaProducer.java` | ➕ Créé - envoie `CARD_CREATED/DELETED` |
| **card-service** | `service/impl/CardServiceImpl.java` | ✏️ Modifié - appelle le producer |
| **loans-service** | `kafka/LoanKafkaProducer.java` | ➕ Créé - envoie `LOAN_CREATED/DELETED` |
| **loans-service** | `service/impl/LoanServiceImpl.java` | ✏️ Modifié - appelle le producer |
| **loans-service** | `kafka/LoanKafkaConsumer.java` | ➕ Créé - écoute `ACCOUNT_DELETED` |
| **loans-service** | `repository/LoanRepository.java` | ✏️ Ajouté `deleteByAccountId()` |
| **loans-service** | `service/LoanService.java` | ✏️ Ajouté `deleteLoanByAccountId()` |

---

## Guide de Test

### Prérequis

- Kafka doit être démarré
- Tous les services doivent être lancés (account, card, loans)

### Scénario de Test Complet

#### 1. Créer un Account
```bash
POST http://localhost:8081/accounts
Content-Type: application/json

{
  "name": "Jean Dupont",
  "email": "jean@test.com",
  "solde": 1000
}
```

**Résultat attendu :** Account créé avec `totalCards=0`, `totalLoans=0`

---

#### 2. Créer une première Card
```bash
POST http://localhost:8082/cards
Content-Type: application/json

{
  "cardNumber": "1234567890123456",
  "cardType": "VISA",
  "accountId": 1
}
```

**Vérifier :** `totalCards` doit être à **1**

---

#### 3. Créer une deuxième Card
```bash
POST http://localhost:8082/cards
Content-Type: application/json

{
  "cardNumber": "9876543210987654",
  "cardType": "MASTERCARD",
  "accountId": 1
}
```

**Vérifier :** `totalCards` doit être à **2**

---

#### 4. Créer un Loan
```bash
POST http://localhost:8083/loans
Content-Type: application/json

{
  "amount": 5000,
  "type": "CONSO",
  "accountId": 1
}
```

**Vérifier :** `totalLoans` doit être à **1**

---

#### 5. Créer un deuxième Loan
```bash
POST http://localhost:8083/loans
Content-Type: application/json

{
  "amount": 10000,
  "type": "IMMO",
  "accountId": 1
}
```

**Vérifier :** `totalLoans` doit être à **2**

---

#### 6. Supprimer une Card
```bash
DELETE http://localhost:8082/cards/1
```

**Vérifier :** `totalCards` doit revenir à **1**

---

#### 7. Supprimer un Loan
```bash
DELETE http://localhost:8083/loans/1
```

**Vérifier :** `totalLoans` doit revenir à **1**

---

#### 8. Supprimer l'Account (Cascade)
```bash
DELETE http://localhost:8081/accounts/1
```

**Vérifier :**
- Toutes les Cards de l'Account doivent être supprimées
- Tous les Loans de l'Account doivent être supprimés

---

## Concepts Clés

### Producer (Producteur)
- **Rôle :** Envoie des messages/events à Kafka
- **Exemple :** `CardKafkaProducer` envoie `CARD_CREATED`

### Consumer (Consommateur)
- **Rôle :** Écoute et traite les messages de Kafka
- **Exemple :** `AccountKafkaConsumer` met à jour les compteurs

### Topic
- **Définition :** Canal de communication Kafka
- **Exemples :** `card-events`, `loan-events`, `account-events`

### @KafkaListener
- **Rôle :** Annotation Spring qui abonne un méthode à un topic
- **Exemple :** `@KafkaListener(topics = "card-events")`

### GroupId
- **Rôle :** Identifie un groupe de consommateurs
- **Permet :** Load balancing entre plusieurs instances du même service

---

## Conclusion

Cette architecture événementielle permet :

1. **Découplage** des services
2. **Communication asynchrone** et non bloquante
3. **Mise à jour en temps réel** des données
4. **Meilleure performance** et résilience
5. **Scalabilité** horizontale des services

C'est un pattern largement utilisé dans les entreprises pour gérer des millions de requêtes par jour avec une architecture microservices.
