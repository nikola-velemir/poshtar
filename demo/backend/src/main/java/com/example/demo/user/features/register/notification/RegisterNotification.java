package com.example.demo.user.features.register.notification;

import org.example.core.notification.INotification;

public record RegisterNotification(String username,String email) implements INotification {
}
