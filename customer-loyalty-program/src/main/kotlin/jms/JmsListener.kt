package sk.bsmk.clp.jms

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component
import sk.bsmk.clp.shared.RegistrationRequestDto
import sk.bsmk.clp.usecases.RegistrationUseCase
import javax.jms.TextMessage

private val logger = KotlinLogging.logger {}

@Component
class JmsListener(
    val objectMapper: ObjectMapper,
    val registration: RegistrationUseCase
) {

    @JmsListener(destination = "registrations")
    fun register(message: TextMessage) {
        logger.info { "Received $message" }
        val request = objectMapper.readValue(message.text, RegistrationRequestDto::class.java)
        registration.register(request)
    }

}