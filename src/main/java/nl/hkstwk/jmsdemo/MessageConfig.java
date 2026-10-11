package nl.hkstwk.jmsdemo;

import jakarta.jms.ConnectionFactory;
import jakarta.jms.Message;
import jakarta.jms.Queue;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;
import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jms.autoconfigure.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.messaging.converter.JacksonJsonMessageConverter;
import org.springframework.messaging.converter.MessageConverter;

@Configuration
@Import(JmsBeansPrinterConfig.class)
public class MessageConfig {
    // @Bean
    // ExampleListener messageListener() {
    //     return new ExampleListener();
    // }

    @Bean // Serialize message content to json using TextMessage
    public MessageConverter jacksonJmsMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public Queue queue() {
        return new ActiveMQQueue("IN_Q");
    }

    @Bean
    public JmsListenerContainerFactory<?> myFactory(
            @Qualifier("jmsConnectionFactory") ConnectionFactory connectionFactory,
            DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setConcurrency("1-5");
        factory.setSessionTransacted(false);
        factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
        factory.setCacheLevel(DefaultMessageListenerContainer.CACHE_CONSUMER);
        factory.setMaxMessagesPerTask(5);
        factory.setReceiveTimeout(5000L);
        return factory;
    }

    // ==================== Programmatische Listeners via JmsListenerConfigurer ====================

    @Bean
    public JmsListenerConfigurer jmsListenerConfigurer(
            @Qualifier("myFactory") JmsListenerContainerFactory<?> myFactory) {
        return new MyJmsListenerConfigurer(myFactory);
    }

    public static class MyJmsListenerConfigurer implements JmsListenerConfigurer {
        private final JmsListenerContainerFactory<?> myFactory;

        public MyJmsListenerConfigurer(JmsListenerContainerFactory<?> myFactory) {
            this.myFactory = myFactory;
        }

        @Override
        public void configureJmsListeners(JmsListenerEndpointRegistrar registrar) {
            // Listener voor TYPE_A
            SimpleJmsListenerEndpoint endpointA = new SimpleJmsListenerEndpoint();
            endpointA.setId("typeA-Listener");
            endpointA.setDestination("IN_Q");
            endpointA.setSelector("JMSType = 'TYPE_A'");
            endpointA.setMessageListener(message -> {
                try {
                    if (message instanceof TextMessage textMessage) {
                        System.out.println("TYPE_A listener ontving: " + textMessage.getText());
                        // Succesvolle verwerking: acknowledge het bericht
                        message.acknowledge();
                    } else {
                        System.out.println("TYPE_A listener ontving niet-tekst bericht");
                        message.acknowledge();
                    }
                } catch (Exception e) {
                    LoggerFactory.getLogger(MessageConfig.class).error("Fout in TYPE_A listener", e);
                    // Geen acknowledge bij fout: bericht blijft op de queue
                }
            });

            // Listener voor TYPE_B
            SimpleJmsListenerEndpoint endpointB = new SimpleJmsListenerEndpoint();
            endpointB.setId("typeB-Listener");
            endpointB.setDestination("IN_Q");
            endpointB.setSelector("JMSType = 'TYPE_B'");
            endpointB.setMessageListener(message -> {
                try {
                    if (message instanceof TextMessage textMessage) {
                        System.out.println("TYPE_B listener ontving: " + textMessage.getText());
                        // Succesvolle verwerking: acknowledge het bericht
                        message.acknowledge();
                    } else {
                        System.out.println("TYPE_B listener ontving niet-tekst bericht");
//                        message.acknowledge();
                    }
                } catch (Exception e) {
                    LoggerFactory.getLogger(MessageConfig.class).error("Fout in TYPE_B listener", e);
                    // Geen acknowledge bij fout: bericht blijft op de queue
                }
            });

            // Listener voor TYPE_C
            SimpleJmsListenerEndpoint endpointC = new SimpleJmsListenerEndpoint();
            endpointC.setId("typeC-Listener");
            endpointC.setDestination("IN_Q");
            endpointC.setSelector("JMSType = 'TYPE_C'");
            endpointC.setMessageListener(message -> {
                try {
                    if (message instanceof TextMessage textMessage) {
                        System.out.println("TYPE_C listener ontving: " + textMessage.getText());
                        // Succesvolle verwerking: acknowledge het bericht
                        message.acknowledge();
                    } else {
                        System.out.println("TYPE_C listener ontving niet-tekst bericht");
//                        message.acknowledge();
                    }
                } catch (Exception e) {
                    LoggerFactory.getLogger(MessageConfig.class).error("Fout in TYPE_C listener", e);
                    // Geen acknowledge bij fout: bericht blijft op de queue
                }
            });

            // Registreer beide endpoints met de fabriek
            registrar.registerEndpoint(endpointA, myFactory);
            registrar.registerEndpoint(endpointB, myFactory);
//            registrar.registerEndpoint(endpointC, myFactory);
        }
    }
}
