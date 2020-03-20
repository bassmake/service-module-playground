package sk.bsmk.clp.domain

import sk.bsmk.clp.domain.points.AddPointsCommand
import sk.bsmk.clp.domain.points.PointsAdjustedEvent
import sk.bsmk.clp.domain.registration.RegistrationCommand
import sk.bsmk.clp.domain.transactions.AddMonetaryTransactionCommand
import sk.bsmk.clp.domain.transactions.MonetaryTransaction
import sk.bsmk.clp.domain.transactions.MonetaryTransactionAddedEvent
import sk.bsmk.clp.shared.LoyaltyTier

data class CustomerEntity(
  val id: CustomerId,
  val data: CustomerData,
  val monetaryTransactions: List<MonetaryTransaction>,
  val version: Int
) {

  companion object {
    fun register(command: RegistrationCommand): CustomerEntity {
      return CustomerEntity(
        id = command.id,
        name = command.name
      )
    }
  }

  constructor(id: CustomerId, name: String) : this(
    id = id,
    data = CustomerData(name),
    monetaryTransactions = emptyList(),
    version = 0
  )

  fun addMonetaryTransaction(command: AddMonetaryTransactionCommand): MonetaryTransactionAddedEvent {
    return MonetaryTransactionAddedEvent(id, command.transaction, 0, LoyaltyTier.GOLD)
  }

  fun addPoints(command: AddPointsCommand): PointsAdjustedEvent {
    val newPoints = data.points + command.pointsToAdd
    val newTier = when {
      newPoints > 100 -> LoyaltyTier.GOLD
      newPoints > 50 -> LoyaltyTier.SILVER
      else -> LoyaltyTier.NONE
    }

    return PointsAdjustedEvent(
      customerId = id,
      points = newPoints,
      tier = newTier,
      version = version + 1
    )
  }


}
