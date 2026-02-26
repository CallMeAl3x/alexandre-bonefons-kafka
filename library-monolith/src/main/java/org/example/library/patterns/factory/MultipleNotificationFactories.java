package org.example.library.patterns.factory;

/**
 * APPROCHE A : Multiple Factory Classes
 *
 * Chaque canal de notification a sa propre factory.
 * Avantage : chaque factory peut avoir sa propre logique d'initialisation.
 * Inconvénient : multiplier les classes si on a beaucoup de types.
 */

// --- Factory pour Email ---
class EmailNotificationFactory {
    public NotificationService create() {
        return new EmailNotificationService();
    }
}

// --- Factory pour SMS ---
class SmsNotificationFactory {
    public NotificationService create() {
        return new SmsNotificationService();
    }
}

// --- Factory pour Push ---
class PushNotificationFactory {
    public NotificationService create() {
        return new PushNotificationService();
    }
}
