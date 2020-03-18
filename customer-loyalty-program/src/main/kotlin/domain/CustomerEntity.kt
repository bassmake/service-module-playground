package sk.bsmk.clp.domain

import sk.bsmk.clp.domain.points.add.AddPointsCommand
import sk.bsmk.clp.domain.registration.RegistrationEvent
import sk.bsmk.clp.shared.LoyaltyTier
import java.util.*

data class CustomerEntity(
  val id: UUID,
  val name: String,
  val tier: LoyaltyTier,
  val points: Int,
  val version: Int
) {

  constructor(event: RegistrationEvent) : this(id = event.id, name = event.name)

  constructor(id: UUID, name: String) : this(
    id = id,
    name = name,
    tier = LoyaltyTier.NONE,
    points = 0,
    version = 1
  )

  fun addPoints(addPointsCommand: AddPointsCommand): PointsAdjustedEvent {
    val newPoints = points + addPointsCommand.pointsToAdd
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
