package sk.bsmk.clp.jms

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.springframework.jms.annotation.JmsListener
import org.springframework.messaging.Message
import org.springframework.stereotype.Component
import sk.bsmk.clp.domain.registration.RegistrationCommand
import sk.bsmk.clp.shared.RegistrationRequestDto
import sk.bsmk.clp.usecases.RegistrationUseCase
import java.util.*
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
        logger.info { "Processing request $request" }
        val command = RegistrationCommand(id = UUID.randomUUID(), name = request.name)
        registration.register(command)
    }

}