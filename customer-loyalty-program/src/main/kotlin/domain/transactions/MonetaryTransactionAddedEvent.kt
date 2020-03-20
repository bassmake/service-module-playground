package sk.bsmk.clp.domain.transactions

import sk.bsmk.clp.domain.CustomerId
import sk.bsmk.clp.shared.LoyaltyTier


data class MonetaryTransactionAddedEvent(
    val customerId: CustomerId,
    val transaction: MonetaryTransaction,
    val newPoints: Int,
    val newTier: LoyaltyTier
)