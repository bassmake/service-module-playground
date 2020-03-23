package sk.bsmk.ct.config;

import com.consol.citrus.dsl.endpoint.CitrusEndpoints;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.jms.endpoint.JmsEndpoint;
import com.consol.citrus.kafka.endpoint.KafkaEndpoint;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.Message;
import org.apache.camel.builder.ExpressionBuilder;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.spring.SpringCamelContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import sk.bsmk.clp.external.CustomerRegistrationReply;
import sk.bsmk.clp.external.CustomerRegistrationRequest;

import javax.jms.ConnectionFactory;
import java.util.Arrays;
import java.util.List;

import static sk.bsmk.ct.config.InfrastructureConfig.MONETARY_TRANSACTIONS_TOPIC;

@Configuration
@Import(InfrastructureConfig.class)
public class EndpointsConfig {

    public static final String REGISTERED_QUEUE = "registered";

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
                .destination(REGISTERED_QUEUE)
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

    @Bean
    public CamelContext camelContext() throws Exception {
        final SpringCamelContext context = new SpringCamelContext();
        final RouteDefinition externalGrpcServiceMock = new RouteDefinition()
                .from("grpc://localhost:8990/sk.bsmk.clp.external.ExternalService?negotiationType=PLAINTEXT")
                .process(exchange -> {
                    Message inMessage = exchange.getIn();
                    CustomerRegistrationRequest request = inMessage.getBody(CustomerRegistrationRequest.class);
                    Message outMessage = exchange.getOut();
                    outMessage.setBody(request);
                    CustomerRegistrationReply reply = CustomerRegistrationReply
                            .newBuilder()
                            .setCode(9342)
                            .setMessage("I am message from citrus component test")
                            .build();
                    inMessage.setBody(reply);
                })
                .to("seda:grpc-ext");

        final RouteDefinition grpcRegistrationRoute = new RouteDefinition()
                .from("direct:grpc-registration-request")
                .to("grpc://localhost:8991/sk.bsmk.clp.grpc.ClpService?method=Register&negotiationType=PLAINTEXT")
                .transform(ExpressionBuilder.bodyExpression(List.class, list -> list.get(0)))
                .to("seda:grpc-registration-response");

        context.addRouteDefinitions(Arrays.asList(externalGrpcServiceMock, grpcRegistrationRoute));

        return context;
    }


}
