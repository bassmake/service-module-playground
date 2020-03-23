package sk.bsmk.ct;

import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.design.TestDesigner;
import com.consol.citrus.dsl.testng.TestNGCitrusTest;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.jms.endpoint.JmsEndpoint;
import com.consol.citrus.kafka.endpoint.KafkaEndpoint;
import com.consol.citrus.kafka.message.KafkaMessageHeaders;
import com.consol.citrus.message.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import sk.bsmk.ct.behaviors.CustomerHttpRegistrationBehavior;
import sk.bsmk.ct.behaviors.RegisteredCustomerCheckBehavior;

import java.math.BigDecimal;

@Test
public class CustomerMonetaryTransactionsTest extends TestNGCitrusTest {

    @Autowired
    private HttpClient customersHttpClient;

    @Autowired
    private KafkaEndpoint monetaryTransactionsTopic;

    @Autowired
    private JmsEndpoint registeredCustomersQueue;

    @Parameters("designer")
    @CitrusTest
    public void receivingMonetaryTransaction(@Optional @CitrusResource TestDesigner designer) {
        designer.variable("customerName", "citrus:concat('customer-name-trx_', citrus:randomNumber(4))");
        designer.variable("transactionAmount", new BigDecimal("10.0"));

        designer.applyBehavior(new CustomerHttpRegistrationBehavior(customersHttpClient));

        designer.applyBehavior(new RegisteredCustomerCheckBehavior(customersHttpClient, registeredCustomersQueue));

        designer.send(monetaryTransactionsTopic)
                .header(KafkaMessageHeaders.MESSAGE_KEY, "not-used")
                .messageType(MessageType.JSON)
                .payload(new ClassPathResource("monetary-transaction/monetaryTransaction.json"));

        designer.sleep(1.0);

        designer.echo("Checking customer detail http response for ${customerName}");

        designer.http().client(customersHttpClient)
                .send().get("/${customerId}");

        designer.variable("customerPoints", 1);
        designer.http().client(customersHttpClient)
                .receive()
                .response(HttpStatus.OK)
                .messageType(MessageType.JSON)
                .validateScript(new ClassPathResource("monetary-transaction/customerDetailValidation.groovy"));

    }

}
