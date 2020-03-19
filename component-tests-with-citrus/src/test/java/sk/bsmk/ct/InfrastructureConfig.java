package sk.bsmk.ct;

import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfrastructureConfig {

    @Bean
    public BrokerService messageBroker() {
        try {
            BrokerService messageBroker = BrokerFactory.createBroker("broker:tcp://localhost:61616");
            messageBroker.setPersistent(false);
            messageBroker.setUseJmx(false);
            return messageBroker;
        } catch (Exception e) {
            throw new BeanCreationException("Failed to create embedded message broker", e);
        }
    }

}
