package sk.bsmk.clp.api

import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import sk.bsmk.clp.domain.registration.RegistrationCommand
import sk.bsmk.clp.persistence.CustomerRepository
import sk.bsmk.clp.shared.RegistrationRequestDto
import sk.bsmk.clp.usecases.RegistrationUseCase
import java.util.*

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/customers")
class CustomerController(
    private val registration: RegistrationUseCase,
    private val repository: CustomerRepository
) {

    @PostMapping("/registration")
    fun registration(@RequestBody registrationRequest: RegistrationRequestDto): CustomerDetailDto {
        val command = RegistrationCommand(id = UUID.randomUUID(), name = registrationRequest.name)
        val entity = registration.register(command)
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