package sk.bsmk.ct;

import com.consol.citrus.dsl.endpoint.CitrusEndpoints;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.jms.endpoint.JmsEndpoint;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

import javax.jms.ConnectionFactory;

@Configuration
public class EndpointsConfig {

    @Bean
    public HttpClient customersClient() {
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
    public JmsEndpoint newIdsQueue() {
        return CitrusEndpoints.
                jms()
                .asynchronous()
                .connectionFactory(connectionFactory())
                .destination("new-ids")
                .build();
    }

}
