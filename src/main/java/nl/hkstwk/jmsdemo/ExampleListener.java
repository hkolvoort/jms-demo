package nl.hkstwk.jmsdemo;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.ObjectMessage;
import jakarta.jms.TextMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ExampleListener implements MessageListener {

    @JmsListener(destination = "IN_Q", selector = "", containerFactory = "myFactory")
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage textMessage) {
                log.info(textMessage.getText());
                // Succesvolle verwerking: acknowledge het bericht
                message.acknowledge();
            }
            else {
                throw new IllegalArgumentException("Message must be of type TextMessage");
            }
        } catch (JMSException ex) {
            log.error("Fout bij verwerken bericht", ex);
            // Geen acknowledge bij fout: bericht blijft op de queue
        }
    }
}