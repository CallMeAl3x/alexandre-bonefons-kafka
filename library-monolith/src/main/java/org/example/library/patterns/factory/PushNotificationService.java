package org.example.library.patterns.factory;

/**
 * Implémentation concrète : envoi par notification Push (mobile).
 */
public class PushNotificationService implements NotificationService {

    @Override
    public void sendNotification(String recipient, String message) {
        System.out.println("[PUSH] → " + recipient + " : " + message);
    }

    @Override
    public String getChannelType() {
        return "PUSH";
    }
}
