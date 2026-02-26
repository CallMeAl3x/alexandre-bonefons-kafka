package org.example.library.patterns.factory;

/**
 * Implémentation concrète : envoi par Email.
 */
public class EmailNotificationService implements NotificationService {

    @Override
    public void sendNotification(String recipient, String message) {
        System.out.println("[EMAIL] → " + recipient + " : " + message);
    }

    @Override
    public String getChannelType() {
        return "EMAIL";
    }
}
