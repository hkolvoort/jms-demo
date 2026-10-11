package nl.hkstwk.jmsdemo;

import jakarta.jms.Queue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
class JmsQueueSenderTest {

    @Autowired
    private Queue queue;

    @Autowired
    private JmsQueueSender sender;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSendMessage() {
        sender.setQueue(queue);

        for (int i = 0; i < 50000; i++) {
            sender.sendEmail(new Email("test@example.com", String.format("Test body %s", i)));
            sender.simpleSend(i);
        }

    }
}