package nl.hkstwk.jmsdemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.core.JmsTemplate;

@SpringBootApplication
@Slf4j
public class JmsDemoApplication {

    public static void main(String[] args) {
        // Launch the application
        ConfigurableApplicationContext context = SpringApplication.run(JmsDemoApplication.class, args);

//        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
//
//        // Send a message with a POJO - the template reuse the message converter
//        log.info("Sending an email message.");
//        jmsTemplate.convertAndSend("mailbox", new Email("info@example.com", "Hello"));
    }

}