package com.example.demo.user.features.register.notification;


import org.nikola.velemir.poshtar.core.notification.INotification;

public record RegisterNotification(String username, String email) implements INotification {
}
