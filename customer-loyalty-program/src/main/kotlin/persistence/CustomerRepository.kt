package sk.bsmk.clp.persistence

import mu.KotlinLogging
import org.springframework.stereotype.Repository
import sk.bsmk.clp.domain.CustomerEntity
import sk.bsmk.clp.domain.CustomerId
import java.util.concurrent.ConcurrentHashMap

private val logger = KotlinLogging.logger {}

@Repository
class CustomerRepository {

    private val storage: MutableMap<CustomerId, CustomerEntity> = ConcurrentHashMap()

    fun store(customerEntity: CustomerEntity) {
        logger.info { "storing $customerEntity" }
        storage[customerEntity.id] = customerEntity
    }

    fun detail(id: CustomerId): CustomerEntity? {
        return storage[id]
    }

}