package sk.bsmk.clp.domain

import sk.bsmk.clp.domain.points.AddPointsCommand
import sk.bsmk.clp.domain.points.PointsAdjustedEvent
import sk.bsmk.clp.domain.registration.RegistrationCommand
import sk.bsmk.clp.domain.transactions.AddMonetaryTransactionCommand
import sk.bsmk.clp.domain.transactions.MonetaryTransaction
import sk.bsmk.clp.domain.transactions.MonetaryTransactionAddedEvent
import sk.bsmk.clp.shared.LoyaltyTier
import java.math.BigDecimal
import java.math.RoundingMode

data class CustomerEntity(
  val id: CustomerId,
  val name: String,
  val tier: LoyaltyTier,
  val points: Int,
  val monetaryTransactions: List<MonetaryTransaction>
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
    name = name,
    tier =  LoyaltyTier.NONE,
    points = 0,
    monetaryTransactions = emptyList()
  )

  // commands
  fun process(command: AddMonetaryTransactionCommand): MonetaryTransactionAddedEvent {
    val pointsToAdd: Int = (command.transaction.amount.divide(BigDecimal(10.0), RoundingMode.DOWN)).toInt()
    val addedPoints = process(AddPointsCommand(pointsToAdd))
    return MonetaryTransactionAddedEvent(id, command.transaction, addedPoints.points, addedPoints.tier)
  }

  fun process(command: AddPointsCommand): PointsAdjustedEvent {
    val newPoints = points + command.pointsToAdd
    val newTier = when {
      newPoints > 100 -> LoyaltyTier.GOLD
      newPoints > 50 -> LoyaltyTier.SILVER
      else -> LoyaltyTier.NONE
    }

    return PointsAdjustedEvent(
      customerId = id,
      points = newPoints,
      tier = newTier
    )
  }

  // events
  fun apply(event: MonetaryTransactionAddedEvent) = this.copy(
    points = event.newPoints,
    tier = event.newTier,
    monetaryTransactions = monetaryTransactions + event.transaction
  )

}
