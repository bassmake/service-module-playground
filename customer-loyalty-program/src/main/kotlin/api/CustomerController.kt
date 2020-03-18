package sk.bsmk.clp.api

import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.jms.core.JmsTemplate
import org.springframework.web.bind.annotation.*
import sk.bsmk.clp.domain.CustomerEntity
import sk.bsmk.clp.persistence.CustomerRepository
import java.util.*

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/customers")
class CustomerController(
    private val repository: CustomerRepository,
    private val jmsTemplate: JmsTemplate
) {

    @PostMapping("/registration")
    fun registration(@RequestBody registration: RegistrationRequest): CustomerDetailDto {
        val id = UUID.randomUUID()
        val entity = CustomerEntity(id, registration.name)
        logger.info { "storing $entity" }
        repository.store(entity)
        jmsTemplate.send("new-ids") { session ->
            session.createTextMessage(String.format("new-id=%s", id));
        }
        return CustomerDetailDto(entity)
    }

    @GetMapping("/{id}")
    fun detail(@PathVariable id: UUID): ResponseEntity<CustomerDetailDto> {
        val entity = repository.detail(id)
        logger.info { "providing detail for $entity" }
        return if (entity == null) {
            ResponseEntity.notFound().build()
        } else {
            ResponseEntity.ok(CustomerDetailDto(entity))
        }
    }

}