package sk.bsmk.clp.shared

import java.math.BigDecimal
import java.util.*

data class MonetaryTransactionDto(
    val customerId: UUID,
    val transactionId: UUID,
    val amount: BigDecimal,
    val currency: String
)