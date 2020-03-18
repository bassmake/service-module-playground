package sk.bsmk.ct;

import com.consol.citrus.dsl.endpoint.CitrusEndpoints;
import com.consol.citrus.http.client.HttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EndpointsConfig {

    @Bean
    public HttpClient customersClient() {
        return CitrusEndpoints
                .http()
                .client()
                .requestUrl("http://localhost:9000/customers")
                .build();
    }

}
