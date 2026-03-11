package poshtar.tests.notification.deps.async;

import org.example.core.annotations.NotificationHandler;
import org.example.core.notification.handler.INotificationHandler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@NotificationHandler
public class FailForAsyncThirdHandler implements INotificationHandler<FailForAsyncNotification> {
    @Override
    @Async
    @Transactional(propagation = Propagation.MANDATORY)
    public void handle(FailForAsyncNotification failForAsyncNotification) {
        throw new RuntimeException("Purposefully causing exception");
    }
}
