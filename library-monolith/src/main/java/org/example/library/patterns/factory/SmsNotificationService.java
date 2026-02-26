package org.example.library.patterns.factory;

/**
 * Implémentation concrète : envoi par SMS.
 */
public class SmsNotificationService implements NotificationService {

    @Override
    public void sendNotification(String recipient, String message) {
        System.out.println("[SMS] → " + recipient + " : " + message);
    }

    @Override
    public String getChannelType() {
        return "SMS";
    }
}
