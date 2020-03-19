package sk.bsmk.clp.usecases

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Service
import sk.bsmk.clp.domain.CustomerEntity
import sk.bsmk.clp.domain.registration.RegistrationCommand
import sk.bsmk.clp.persistence.CustomerRepository

private val logger = KotlinLogging.logger {}

@Service
class RegistrationUseCase(
    val objectMapper: ObjectMapper,
    val jmsTemplate: JmsTemplate,
    val repository: CustomerRepository
) {

    fun register(command: RegistrationCommand): CustomerEntity {
        val entity = CustomerEntity.register(command)
        logger.info { "storing $entity" }
        repository.store(entity)
        jmsTemplate.send("registered") { session ->
            val json = objectMapper.writeValueAsString(entity)
            logger.info { "publishing to queue $json" }
            session.createObjectMessage(json)
        }
        return entity
    }

}