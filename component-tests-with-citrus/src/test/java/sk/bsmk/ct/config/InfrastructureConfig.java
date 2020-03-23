package sk.bsmk.ct.config;

import com.consol.citrus.kafka.embedded.EmbeddedKafkaServer;
import com.consol.citrus.kafka.embedded.EmbeddedKafkaServerBuilder;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfrastructureConfig {

    public static final String MONETARY_TRANSACTIONS_TOPIC = "monetary-transactions";

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

    @Bean
    public EmbeddedKafkaServer kafkaServer() {
        return new EmbeddedKafkaServerBuilder()
                .topics(MONETARY_TRANSACTIONS_TOPIC)
                .build();
    }

}
