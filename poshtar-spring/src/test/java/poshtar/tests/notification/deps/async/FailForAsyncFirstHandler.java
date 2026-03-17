package poshtar.tests.notification.deps.async;


import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.notification.handler.NotificationHandler;

@Handler
public class FailForAsyncFirstHandler implements NotificationHandler<FailForAsyncNotification> {
    @Override
    public void handle(FailForAsyncNotification failForAsyncNotification) {
        System.out.println("First handler called!");

    }
}
