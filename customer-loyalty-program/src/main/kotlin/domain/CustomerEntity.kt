package sk.bsmk.clp.domain

data class CustomerEntity(
  val id: Int,
  val name: String,
  val tier: CustomerTier,
  val points: Int,
  val version: Int
) {

  constructor(id: Int, name: String) : this(
    id = id,
    name = name,
    tier = CustomerTier.NONE,
    points = 0,
    version = 1
  )

  fun addPoints(addPointsCommand: AddPointsCommand): PointsAdjustedEvent {
    val newPoints = points + addPointsCommand.pointsToAdd
    val newTier = when {
      newPoints > 100 -> CustomerTier.GOLD
      newPoints > 50 -> CustomerTier.SILVER
      else -> CustomerTier.NONE
    }

    return PointsAdjustedEvent(
      customerId = id,
      points = newPoints,
      tier = newTier,
      version = version + 1
    )
  }

  fun syncWithExternal(command: SyncWithExternalSystemCommand): DataSyncedEvent {
    val infoToUpdate = if (tier == CustomerTier.GOLD) {
      command.getDataCall()
    } else {
      null
    }
    return DataSyncedEvent(infoToUpdate)
  }


}
