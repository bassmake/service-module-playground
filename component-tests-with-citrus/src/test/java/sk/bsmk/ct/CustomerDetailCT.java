package sk.bsmk.ct;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.testng.TestNGCitrusTestDesigner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;

import java.util.UUID;

@Test
public class CustomerDetailCT extends TestNGCitrusTestDesigner {

    @Autowired
    private HttpClient customersClient;

    @Test
    @CitrusTest
    public void customerDetail() {

        final UUID customerId = UUID.randomUUID();
        final String customerName = "TODO";
        variable("customerId", customerId);
        variable("customerName", customerName);

        http().client(customersClient)
                .send().get("/" + customerId);

        http().client(customersClient)
                .receive()
                .response(HttpStatus.OK)
                .messageType(MessageType.JSON)
                .validateScript(new ClassPathResource("customerDetailValidation.groovy"));

    }

}
