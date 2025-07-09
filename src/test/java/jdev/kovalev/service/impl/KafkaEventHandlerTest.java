package jdev.kovalev.service.impl;

import jdev.kovalev.dto.kafka.EventForNotificationSrv;
import jdev.kovalev.service.EmailSendingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@EmbeddedKafka(
        partitions = 1,
        topics = "confirmation-codes-from-authsrv-to-notification-srv",
        brokerProperties = {
        "offsets.topic.replication.factor=1",
        "transaction.state.log.replication.factor=1",
        "min.insync.replicas=1"
})
class KafkaEventHandlerTest {
    public static final String TOPIC_NAME = "confirmation-codes-from-authsrv-to-notification-srv";

    @Autowired
    private KafkaTemplate<String, EventForNotificationSrv> kafkaTemplate;

    @MockitoBean
    private EmailSendingService emailSendingService;

    @Test
    void handle() throws InterruptedException {
        EventForNotificationSrv event = EventForNotificationSrv.builder()
                .email("test@example.com")
                .confirmationCode("123456")
                .build();

        kafkaTemplate.send(TOPIC_NAME, event);
        kafkaTemplate.flush();

        TimeUnit.SECONDS.sleep(5);

        verify(emailSendingService).sendEmail(eq(event.email()), eq(event.confirmationCode()));
    }
}