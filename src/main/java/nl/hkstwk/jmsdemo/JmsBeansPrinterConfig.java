package nl.hkstwk.jmsdemo;

import jakarta.jms.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.JmsListenerEndpoint;

import java.util.Arrays;

@Configuration
public class JmsBeansPrinterConfig {
    @Bean
    public CommandLineRunner printJmsBeans(ApplicationContext context) {
        return args -> {
            Logger log = LoggerFactory.getLogger(JmsBeansPrinterConfig.class);

            String[] jmsFactoryNames = context.getBeanNamesForType(ConnectionFactory.class);
            log.info("--- ConnectionFactory beans ---");
            Arrays.stream(jmsFactoryNames).forEach(log::info);

            String[] allJmsEndPoints = context.getBeanNamesForType(JmsListenerEndpoint.class);
            log.info("--- ALL JmsListenerEndpoint beans ---");
            Arrays.stream(allJmsEndPoints).forEach(log::info);
        };
    }
}
