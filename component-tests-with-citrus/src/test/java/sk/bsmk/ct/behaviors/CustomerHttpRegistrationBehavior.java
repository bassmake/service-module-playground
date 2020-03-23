package sk.bsmk.ct.behaviors;

import com.consol.citrus.dsl.design.AbstractTestBehavior;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.MessageType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class CustomerHttpRegistrationBehavior extends AbstractTestBehavior {

    private final HttpClient customersHttpClient;

    public CustomerHttpRegistrationBehavior(HttpClient customersHttpClient) {
        this.customersHttpClient = customersHttpClient;
    }

    @Override
    public void apply() {

        echo("Sending http registration request for ${customerName}");
        http().client(customersHttpClient)
                .send().post("/registration")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .payload(new ClassPathResource("registration/request.json"));


        echo("Checking http response for ${customerName}");
        http().client(customersHttpClient)
                .receive()
                .response(HttpStatus.OK)
                .messageType(MessageType.JSON)
                .validateScript(new ClassPathResource("registration/customerRegistrationResponseValidation.groovy"));

    }
}
