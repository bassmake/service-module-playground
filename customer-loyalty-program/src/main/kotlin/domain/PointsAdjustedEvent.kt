package sk.bsmk.clp.domain

import sk.bsmk.clp.shared.LoyaltyTier
import java.util.*

data class PointsAdjustedEvent(
  val customerId: UUID,
  val points: Int,
  val tier: LoyaltyTier,
  val version: Int
)
