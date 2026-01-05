package com.assessment.pm.infrastructure.adapter.output.audit;

import com.assessment.pm.domain.ports.out.AuditLogPort;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.logging.Logger;

@Component
public class ConsoleAuditAdapter implements AuditLogPort {

    private static final Logger logger = Logger.getLogger(ConsoleAuditAdapter.class.getName());

    @Override
    public void register(String action, UUID entityId) {
        logger.info("AUDIT: Action=" + action + ", EntityId=" + entityId);
    }
}
