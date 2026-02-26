package org.example.library.patterns.factory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pattern 2 — Factory")
class NotificationFactoryTest {

    @Test
    @DisplayName("Simple Factory crée un EmailNotificationService")
    void shouldCreateEmailService() {
        NotificationService svc = NotificationFactory.create("EMAIL");
        assertInstanceOf(EmailNotificationService.class, svc);
        assertEquals("EMAIL", svc.getChannelType());
    }

    @Test
    @DisplayName("Simple Factory crée un SmsNotificationService")
    void shouldCreateSmsService() {
        NotificationService svc = NotificationFactory.create("SMS");
        assertInstanceOf(SmsNotificationService.class, svc);
        assertEquals("SMS", svc.getChannelType());
    }

    @Test
    @DisplayName("Simple Factory crée un PushNotificationService")
    void shouldCreatePushService() {
        NotificationService svc = NotificationFactory.create("PUSH");
        assertInstanceOf(PushNotificationService.class, svc);
        assertEquals("PUSH", svc.getChannelType());
    }

    @Test
    @DisplayName("La Factory est insensible à la casse")
    void shouldBeCaseInsensitive() {
        assertInstanceOf(EmailNotificationService.class, NotificationFactory.create("email"));
        assertInstanceOf(SmsNotificationService.class,   NotificationFactory.create("sms"));
        assertInstanceOf(PushNotificationService.class,  NotificationFactory.create("Push"));
    }

    @Test
    @DisplayName("Un type inconnu lève une IllegalArgumentException")
    void shouldThrowForUnknownType() {
        assertThrows(IllegalArgumentException.class,
                () -> NotificationFactory.create("FOOBAR"));
    }

    @Test
    @DisplayName("sendNotification ne lève pas d'exception")
    void shouldSendWithoutException() {
        assertDoesNotThrow(() ->
                NotificationFactory.create("EMAIL")
                        .sendNotification("test@example.com", "Test message")
        );
    }
}
