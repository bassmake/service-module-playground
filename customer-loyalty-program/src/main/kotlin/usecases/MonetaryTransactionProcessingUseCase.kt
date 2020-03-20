package sk.bsmk.clp.usecases

import mu.KotlinLogging
import org.springframework.stereotype.Service
import sk.bsmk.clp.domain.CustomerId
import sk.bsmk.clp.domain.transactions.AddMonetaryTransactionCommand
import sk.bsmk.clp.domain.transactions.Currencies
import sk.bsmk.clp.domain.transactions.MonetaryTransaction
import sk.bsmk.clp.persistence.CustomerRepository
import sk.bsmk.clp.shared.MonetaryTransactionDto

private val logger = KotlinLogging.logger {}

@Service
class MonetaryTransactionProcessingUseCase(
    val repository: CustomerRepository
) {

    fun process(transactionDto: MonetaryTransactionDto) {
        val currency = Currencies.parse(transactionDto.currency)
        if (currency == null) {
            logger.warn { "Unsupported currency=${transactionDto.currency}" }
            return
        }

        val customerId = CustomerId(transactionDto.customerId)
        val entity = repository.detail(customerId)
        if (entity == null) {
            logger.warn { "Customer with id=$customerId was not registered" }
            return
        }

        val transaction = MonetaryTransaction(
            id = transactionDto.transactionId,
            amount = transactionDto.amount,
            currency = currency
        )

        val event = entity.process(AddMonetaryTransactionCommand(transaction))
        val newState = entity.apply(event)
        repository.store(newState)
    }

}