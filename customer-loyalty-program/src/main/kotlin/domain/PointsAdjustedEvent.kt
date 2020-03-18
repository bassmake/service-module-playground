package sk.bsmk.clp.domain

data class PointsAdjustedEvent(
  val customerId: Int,
  val points: Int,
  val tier: CustomerTier,
  val version: Int
)
