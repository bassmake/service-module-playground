package sk.bsmk.ct;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.testng.TestNGCitrusTestDesigner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.jms.endpoint.JmsEndpoint;
import com.consol.citrus.message.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.Test;
import sk.bsmk.ct.behaviors.CustomerRegisteredCheckBehavior;

@Test
public class CustomerRegistrationTest extends TestNGCitrusTestDesigner {

    @Autowired
    private HttpClient customersClient;

    @Autowired
    private JmsEndpoint registrationsQueue;

    @Autowired
    private JmsEndpoint registeredCustomersQueue;

    @Test
    @CitrusTest
    public void httpRegistration() {

        variable("customerName", "citrus:concat('customer-name_', citrus:randomNumber(4))");

        echo("Sending http registration request for ${customerName}");
        http().client(customersClient)
                .send().post("/registration")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .payload(new ClassPathResource("registration/request.json"));

        applyBehavior(new CustomerRegisteredCheckBehavior(customersClient, registeredCustomersQueue));

        echo("Checking http response for ${customerName}");
        http().client(customersClient)
                .receive()
                .response(HttpStatus.OK)
                .messageType(MessageType.JSON)
                .validateScript(new ClassPathResource("customerDetailValidation.groovy"));
    }

    @Test
    @CitrusTest
    public void jmsRegistration() {

        variable("customerName", "citrus:concat('customer-name_', citrus:randomNumber(4))");

        echo("Sending registration via queue for ${customerName}");
        send(registrationsQueue)
                .messageType(MessageType.JSON)
                .payload(new ClassPathResource("registration/request.json"));

        applyBehavior(new CustomerRegisteredCheckBehavior(customersClient, registeredCustomersQueue));
    }

}
