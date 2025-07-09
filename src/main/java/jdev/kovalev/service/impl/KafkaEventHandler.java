package jdev.kovalev.service.impl;

import jdev.kovalev.dto.kafka.EventForNotificationSrv;
import jdev.kovalev.service.EmailSendingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "confirmation-codes-from-authsrv-to-notification-srv")
@RequiredArgsConstructor
public class KafkaEventHandler {
    private final EmailSendingService sendingService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @KafkaHandler
    public void handle(EventForNotificationSrv event) {
        logger.info("Handle event: {}", event.toString());
        sendingService.sendEmail(event.email(), event.confirmationCode());
    }
}
