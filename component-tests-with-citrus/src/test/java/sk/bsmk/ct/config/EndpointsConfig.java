package sk.bsmk.ct.config;

import com.consol.citrus.dsl.endpoint.CitrusEndpoints;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.jms.endpoint.JmsEndpoint;
import com.consol.citrus.kafka.endpoint.KafkaEndpoint;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.jms.ConnectionFactory;

import static sk.bsmk.ct.config.InfrastructureConfig.MONETARY_TRANSACTIONS_TOPIC;

@Configuration
@Import(InfrastructureConfig.class)
public class EndpointsConfig {

    @Bean
    public HttpClient customersHttpClient() {
        return CitrusEndpoints
                .http()
                .client()
                .requestUrl("http://localhost:9000/customers")
                .build();
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        return new ActiveMQConnectionFactory("tcp://localhost:61616");
    }

    @Bean
    public JmsEndpoint registeredCustomersQueue() {
        return CitrusEndpoints.
                jms()
                .asynchronous()
                .connectionFactory(connectionFactory())
                .destination("registered")
                .build();
    }

    @Bean
    public JmsEndpoint registrationsQueue() {
        return CitrusEndpoints.
                jms()
                .asynchronous()
                .connectionFactory(connectionFactory())
                .destination("registrations")
                .build();
    }

    @Bean
    public KafkaEndpoint monetaryTransactionsTopic() {
        return CitrusEndpoints.kafka()
                .asynchronous()
                .topic(MONETARY_TRANSACTIONS_TOPIC)
                .server("localhost:9092")
                .build();
    }

}
