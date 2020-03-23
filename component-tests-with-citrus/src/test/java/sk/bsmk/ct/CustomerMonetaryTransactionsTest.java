package sk.bsmk.ct;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.testng.TestNGCitrusTestDesigner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.kafka.endpoint.KafkaEndpoint;
import com.consol.citrus.kafka.message.KafkaMessageHeaders;
import com.consol.citrus.message.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;
import sk.bsmk.ct.behaviors.CustomerHttpRegistrationBehavior;

import java.math.BigDecimal;

@Test
public class CustomerMonetaryTransactionsTest extends TestNGCitrusTestDesigner {

    @Autowired
    private HttpClient customersHttpClient;

    @Autowired
    private KafkaEndpoint monetaryTransactionsTopic;

    @Test
    @CitrusTest
    public void receivingMonetaryTransaction() {

        variable("customerName", "citrus:concat('customer-name_', citrus:randomNumber(4))");
        variable("transactionAmount", new BigDecimal("10.0"));

        applyBehavior(new CustomerHttpRegistrationBehavior(customersHttpClient));

        send(monetaryTransactionsTopic)
                .header(KafkaMessageHeaders.MESSAGE_KEY, "not-used")
                .messageType(MessageType.JSON)
                .payload(new ClassPathResource("monetary-transaction/monetaryTransaction.json"));

        sleep(1.0);

        echo("Checking customer detail http response for ${customerName}");

        http().client(customersHttpClient)
                .send().get("/${customerId}");

        variable("customerPoints", 1);
        http().client(customersHttpClient)
                .receive()
                .response(HttpStatus.OK)
                .messageType(MessageType.JSON)
                .validateScript(new ClassPathResource("monetary-transaction/customerDetailValidation.groovy"));

    }

}
