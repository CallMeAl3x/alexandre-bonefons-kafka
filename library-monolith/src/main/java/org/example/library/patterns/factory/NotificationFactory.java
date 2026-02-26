package org.example.library.patterns.factory;

/**
 * APPROCHE B : Simple Factory Pattern
 *
 * Une seule classe factory centralise la création de tous les types.
 * Le client appelle NotificationFactory.create("EMAIL") sans connaître
 * les classes concrètes.
 *
 * Avantage : un seul point d'entrée, facile à maintenir et à étendre
 *            (il suffit d'ajouter un case et une nouvelle classe).
 * Inconvénient : modification de la factory à chaque nouveau type
 *                → peut violer OCP (cf. Exercice 2).
 *
 * RÉPONSE : L'Approche B (Simple Factory) est plus flexible pour ajouter un
 * nouveau type car on n'a qu'un seul endroit à modifier au lieu de créer
 * une nouvelle factory entière.
 */
public class NotificationFactory {

    /**
     * Crée un NotificationService selon le type demandé.
     *
     * @param type "EMAIL", "SMS" ou "PUSH" (insensible à la casse)
     * @return l'implémentation correspondante
     * @throws IllegalArgumentException si le type est inconnu
     */
    public static NotificationService create(String type) {
        return switch (type.toUpperCase()) {
            case "EMAIL" -> new EmailNotificationService();
            case "SMS"   -> new SmsNotificationService();
            case "PUSH"  -> new PushNotificationService();
            default      -> throw new IllegalArgumentException(
                    "Type de notification inconnu : " + type +
                    ". Types supportés : EMAIL, SMS, PUSH");
        };
    }
}
