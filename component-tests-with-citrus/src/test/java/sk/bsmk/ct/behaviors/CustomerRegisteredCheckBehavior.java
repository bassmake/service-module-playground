package sk.bsmk.ct.behaviors;

import com.consol.citrus.dsl.design.AbstractTestBehavior;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.jms.endpoint.JmsEndpoint;
import com.consol.citrus.message.MessageType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;

public class CustomerRegisteredCheckBehavior extends AbstractTestBehavior {

    public CustomerRegisteredCheckBehavior(
            HttpClient customersClient,
            JmsEndpoint registeredCustomersQueue
    ) {
        this.customersClient = customersClient;
        this.registeredCustomersQueue = registeredCustomersQueue;
    }

    private final HttpClient customersClient;

    private final JmsEndpoint registeredCustomersQueue;

    public void apply() {

        echo("Checking registered customers queue for ${customerName}");
        receive(registeredCustomersQueue)
                .messageType(MessageType.JSON)
                .validateScript(new ClassPathResource("registration/customerRegistrationResponseValidation.groovy"));

        echo("Sending http GET for ${customerName}");
        http().client(customersClient)
                .send().get("/${customerId}");

        echo("Checking http detail response for ${customerName}");
        http().client(customersClient)
                .receive()
                .response(HttpStatus.OK)
                .messageType(MessageType.JSON)
                .validateScript(new ClassPathResource("customerDetailValidation.groovy"));

    }
}
