package org.example.library.patterns.factory;

/**
 * Interface commune pour tous les services de notification.
 * Le client ne connaît que cette interface, pas les implémentations concrètes.
 */
public interface NotificationService {

    /**
     * Envoie une notification à un destinataire.
     *
     * @param recipient identifiant du destinataire (email, numéro, token...)
     * @param message   contenu du message
     */
    void sendNotification(String recipient, String message);

    /**
     * Type de canal utilisé.
     */
    String getChannelType();
}
