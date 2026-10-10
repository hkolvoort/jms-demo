package nl.hkstwk.jmsdemo;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.Queue;
import jakarta.jms.Session;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.NonNull;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JmsQueueSender {

    private static final String[] JMS_TYPES = new String[]{"TYPE_A", "TYPE_B", "TYPE_C"};
    private final JmsTemplate jmsTemplate;

    @Setter
    private Queue queue;

    public void simpleSend(int i) {
        this.jmsTemplate.send(this.queue, new MessageCreator() {
            public @NonNull Message createMessage(@NonNull Session session) throws JMSException {
                Message message = session.createTextMessage("Bladibla-" + i);
                message.setJMSType(JMS_TYPES[new Random().nextInt(JMS_TYPES.length)]);
                message.setJMSCorrelationID("CORR_ID_" + i);
                return message;
            }
        });
    }

    public void sendEmail(Email email) {
        this.jmsTemplate.convertAndSend(this.queue, email);
    }
}