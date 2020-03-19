package sk.bsmk.clp.jms

import mu.KotlinLogging
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component
import sk.bsmk.clp.domain.registration.RegistrationCommand
import sk.bsmk.clp.shared.RegistrationRequestDto
import sk.bsmk.clp.usecases.RegistrationUseCase
import java.util.*

private val logger = KotlinLogging.logger {}

@Component
class JmsListener(val registration: RegistrationUseCase) {

    @JmsListener(destination = "registrations")
    fun register(request: RegistrationRequestDto) {
        logger.info { "Received $request" }
        val command = RegistrationCommand(id = UUID.randomUUID(), name = request.name)
        registration.register(command)
    }

}