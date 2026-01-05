package com.assessment.pm.infrastructure.adapter.output.notification;

import com.assessment.pm.domain.ports.out.NotificationPort;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class ConsoleNotificationAdapter implements NotificationPort {

    private static final Logger logger = Logger.getLogger(ConsoleNotificationAdapter.class.getName());

    @Override
    public void notify(String message) {
        logger.info("NOTIFICATION: " + message);
    }
}
