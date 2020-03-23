package sk.bsmk.ct;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.testng.TestNGCitrusTestDesigner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.jms.endpoint.JmsEndpoint;
import com.consol.citrus.message.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.testng.annotations.Test;
import sk.bsmk.ct.behaviors.CustomerHttpRegistrationBehavior;
import sk.bsmk.ct.behaviors.RegisteredCustomerCheckBehavior;

@Test
public class CustomerRegistrationTest extends TestNGCitrusTestDesigner {

    @Autowired
    private HttpClient customersHttpClient;

    @Autowired
    private JmsEndpoint registrationsQueue;

    @Autowired
    private JmsEndpoint registeredCustomersQueue;

    @Test
    @CitrusTest
    public void httpRegistration() {

        variable("customerName", "citrus:concat('customer-name_', citrus:randomNumber(4))");

        applyBehavior(new CustomerHttpRegistrationBehavior(customersHttpClient));

        applyBehavior(new RegisteredCustomerCheckBehavior(customersHttpClient, registeredCustomersQueue));

    }

    @Test
    @CitrusTest
    public void jmsRegistration() {

        variable("customerName", "citrus:concat('customer-name_', citrus:randomNumber(4))");

        echo("Sending registration via queue for ${customerName}");
        send(registrationsQueue)
            .messageType(MessageType.JSON)
            .payload(new ClassPathResource("registration/request.json"));

        applyBehavior(new RegisteredCustomerCheckBehavior(customersHttpClient, registeredCustomersQueue));
    }

}
