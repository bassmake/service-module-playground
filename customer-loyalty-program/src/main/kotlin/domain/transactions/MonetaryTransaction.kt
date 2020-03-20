package sk.bsmk.clp.domain.transactions

import java.math.BigDecimal
import java.util.*


data class MonetaryTransaction(val id: UUID, val amount: BigDecimal, val currency: Currency)