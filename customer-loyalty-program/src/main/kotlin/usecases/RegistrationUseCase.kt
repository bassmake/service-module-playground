package sk.bsmk.clp.usecases

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Service
import sk.bsmk.clp.domain.CustomerEntity
import sk.bsmk.clp.domain.CustomerId
import sk.bsmk.clp.domain.registration.RegistrationCommand
import sk.bsmk.clp.grpc.ExternalServiceGrpcClient
import sk.bsmk.clp.persistence.CustomerRepository
import java.util.*

private val logger = KotlinLogging.logger {}

@Service
class RegistrationUseCase(
    val objectMapper: ObjectMapper,
    val jmsTemplate: JmsTemplate,
    val repository: CustomerRepository,
    val externalService: ExternalServiceGrpcClient
) {

    fun register(name: String): CustomerEntity {
        logger.info { "Processing request for $name" }
        val customerId = CustomerId(UUID.randomUUID())
        val command = RegistrationCommand(customerId, name)
        val entity = CustomerEntity.register(command)
        repository.store(entity)
        jmsTemplate.send("registered") { session ->
            val json = objectMapper.writeValueAsString(entity)
            logger.info { "publishing to queue $json" }
            session.createObjectMessage(json)
        }
        externalService.sendRegistration(customerId, name)
        return entity
    }

}