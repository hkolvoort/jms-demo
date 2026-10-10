package nl.hkstwk.jmsdemo;

import jakarta.jms.Queue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JmsQueueSenderTest {

    @Autowired
    private Queue queue;

    @Autowired
    private JmsQueueSender sender;

    @Test
    void testSendMessage() {
        sender.setQueue(queue);
//        sender.simpleSend();

        for (int i = 0; i < 1000; i++) {
            sender.sendEmail(new Email("test@example.com", String.format("Test body %s", i)));
            sender.simpleSend(i);
        }

    }
}