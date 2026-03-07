package org.example.impl.notification;

import org.example.core.notification.handler.INotificationHandler;
import org.example.core.types.Unit;
import org.example.core.annotations.NotificationHandler;

@NotificationHandler
public class OtherNotificationHandler  implements INotificationHandler<BasicNotification> {
    @Override
    public void handle(BasicNotification notification) {
        System.out.println("Notification handler other received notification: " + notification);
    }}