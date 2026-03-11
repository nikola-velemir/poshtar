package com.example.demo.infra.scheduler;

import com.example.demo.infra.scheduler.notification.ScheduledNotification;
import org.example.core.mediator.IMediator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NotificationScheduler {
    @Autowired
    private final IMediator mediator;

    public NotificationScheduler(IMediator mediator) {
        this.mediator = mediator;
    }

    @Scheduled(fixedRate = 15000)
    public void runScheduler(){
        ScheduledNotification notification = new ScheduledNotification();
        mediator.publish(notification);
    }
}
