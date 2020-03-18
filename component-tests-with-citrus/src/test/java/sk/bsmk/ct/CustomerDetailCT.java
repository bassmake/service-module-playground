package sk.bsmk.ct;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.testng.TestNGCitrusTestDesigner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.Test;

import java.util.UUID;

@Test
public class CustomerDetailCT extends TestNGCitrusTestDesigner {

    @Autowired
    private HttpClient customersClient;

    @Test
    @CitrusTest
    public void customerRegistration() {

        final String customerName = "someone-" + UUID.randomUUID();

        variable("customerName", customerName);

        http().client(customersClient)
                .send().post("/registration")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .payload(new ClassPathResource("registration/request.json"));

        http().client(customersClient)
                .receive()
                .response(HttpStatus.OK)
                .messageType(MessageType.JSON)
                .validateScript(new ClassPathResource("registration/customerRegistrationResponseValidation.groovy"));

        http().client(customersClient)
                .send().get("/${customerId}");

        http().client(customersClient)
                .receive()
                .response(HttpStatus.OK)
                .messageType(MessageType.JSON)
                .validateScript(new ClassPathResource("customerDetailValidation.groovy"));
    }

}
