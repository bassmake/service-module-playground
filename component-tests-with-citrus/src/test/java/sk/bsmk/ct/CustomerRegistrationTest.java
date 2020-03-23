package sk.bsmk.ct;

import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.dsl.design.TestDesigner;
import com.consol.citrus.dsl.testng.TestNGCitrusTest;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.jms.endpoint.JmsEndpoint;
import com.consol.citrus.message.DefaultMessage;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.validation.callback.AbstractValidationCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.testng.Assert;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import sk.bsmk.clp.grpc.ClpCustomerGrpcDetail;
import sk.bsmk.clp.grpc.ClpRegistrationRequest;
import sk.bsmk.ct.behaviors.CustomerHttpRegistrationBehavior;
import sk.bsmk.ct.behaviors.RegisteredCustomerCheckBehavior;

import java.util.Map;
import java.util.UUID;

@Test
public class CustomerRegistrationTest extends TestNGCitrusTest {

    @Autowired
    private HttpClient customersHttpClient;

    @Autowired
    private JmsEndpoint registrationsQueue;

    @Autowired
    private JmsEndpoint registeredCustomersQueue;

    @Parameters("designer")
    @CitrusTest
    public void grpcRegistration(@Optional @CitrusResource TestDesigner designer) {
        final String customerName = "customer-name-grpc_" + UUID.randomUUID();
        designer.variable("customerName", customerName);

        designer.send("camel:direct:grpc-registration-request")
                .message(new DefaultMessage(
                        ClpRegistrationRequest.newBuilder()
                                .setCustomerName(customerName)
                                .build()
                ));

        designer.receive("camel:seda:grpc-registration-response")
                .validationCallback(new AbstractValidationCallback<ClpCustomerGrpcDetail>() {
                    @Override
                    public void validate(ClpCustomerGrpcDetail payload, Map<String, Object> headers, TestContext context) {
                        Assert.assertEquals(customerName, payload.getName());
                        Assert.assertEquals(0, payload.getPoints());
                        Assert.assertEquals("NONE", payload.getTier());
                    }
                });

        designer.applyBehavior(new RegisteredCustomerCheckBehavior(customersHttpClient, registeredCustomersQueue));
    }

    @Parameters("designer")
    @CitrusTest
    public void httpRegistration(@Optional @CitrusResource TestDesigner designer) {

        designer.variable("customerName", "citrus:concat('customer-name-http_', citrus:randomNumber(4))");

        designer.applyBehavior(new CustomerHttpRegistrationBehavior(customersHttpClient));

        designer.applyBehavior(new RegisteredCustomerCheckBehavior(customersHttpClient, registeredCustomersQueue));
    }

    @Parameters("designer")
    @CitrusTest
    public void jmsRegistration(@Optional @CitrusResource TestDesigner designer) {
        designer.variable("customerName", "citrus:concat('customer-name-jms_', citrus:randomNumber(4))");

        designer.echo("Sending registration via queue for ${customerName}");
        designer.send(registrationsQueue)
                .messageType(MessageType.JSON)
                .payload(new ClassPathResource("registration/request.json"));

        designer.applyBehavior(new RegisteredCustomerCheckBehavior(customersHttpClient, registeredCustomersQueue));
    }

}
