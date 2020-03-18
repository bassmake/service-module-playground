package sk.bsmk.clp.persistence

import org.springframework.stereotype.Repository
import sk.bsmk.clp.domain.CustomerEntity
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Repository
class CustomerRepository {

    private val storage: MutableMap<UUID, CustomerEntity> = ConcurrentHashMap()

    fun store(customerEntity: CustomerEntity) {
        storage[customerEntity.id] = customerEntity
    }

    fun detail(id: UUID): CustomerEntity? {
        return storage[id]
    }

}