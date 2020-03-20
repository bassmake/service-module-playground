package sk.bsmk.clp.domain.points

import sk.bsmk.clp.domain.CustomerId
import sk.bsmk.clp.shared.LoyaltyTier

data class PointsAdjustedEvent(
  val customerId: CustomerId,
  val points: Int,
  val tier: LoyaltyTier
)
