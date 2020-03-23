package sk.bsmk.clp


import org.awaitility.kotlin.await
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.test.EmbeddedKafkaBroker
import org.springframework.kafka.test.context.EmbeddedKafka
import sk.bsmk.clp.kafka.KafkaListener.Companion.MONETARY_TRANSACTIONS_TOPIC
import sk.bsmk.clp.shared.CustomerDetailDto
import sk.bsmk.clp.shared.MonetaryTransactionDto
import sk.bsmk.clp.shared.RegistrationRequestDto
import java.math.BigDecimal
import java.time.Duration
import java.util.*

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@EmbeddedKafka(
    partitions = 1,
    topics = [MONETARY_TRANSACTIONS_TOPIC]
)
@Disabled("not working yet")
class MonetaryTransactionConsumerTest(
    @Autowired val restTemplate: TestRestTemplate,
    @Autowired val kafkaTemplate: KafkaTemplate<String, MonetaryTransactionDto>,
    @Autowired val embeddedKafka: EmbeddedKafkaBroker
) {

    @Test
    fun `that monetary transaction is processed`() {

        val registrationRequest = RegistrationRequestDto(name = "John Doe")
        val detail = restTemplate.postForObject("/customers/registration", registrationRequest, CustomerDetailDto::class.java)
        Assertions.assertEquals(0, detail.points)

        val transaction = MonetaryTransactionDto(
            customerId = detail.id,
            transactionId = UUID.randomUUID(),
            amount = BigDecimal(10),
            currency = "EUR"
        )

        kafkaTemplate.send(MONETARY_TRANSACTIONS_TOPIC, transaction)

        await
            .atMost(Duration.ofSeconds(2))
            .untilAsserted {
                val points = restTemplate
                    .getForObject("/customers/{id}", CustomerDetailDto::class.java, detail.id)
                    .points
                Assertions.assertEquals(1, points)
            }

    }

}