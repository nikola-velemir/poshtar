package org.example.impl.notification;

import org.example.core.annotations.Notification;
import org.example.core.notification.INotification;

@Notification
public record BasicNotification() implements INotification {
}
