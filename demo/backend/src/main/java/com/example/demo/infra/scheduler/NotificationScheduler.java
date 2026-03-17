package com.example.demo.infra.scheduler;

import com.example.demo.infra.scheduler.notification.ScheduledNotification;
import org.nikola.velemir.poshtar.core.mediator.Poshtar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NotificationScheduler {
    @Autowired
    private final Poshtar poshtar;

    public NotificationScheduler(Poshtar poshtar) {
        this.poshtar = poshtar;
    }

    @Scheduled(fixedRate = 15000)
    public void runScheduler(){
        ScheduledNotification notification = new ScheduledNotification();
        poshtar.publish(notification);
    }
}
