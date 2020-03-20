package sk.bsmk.clp.shared

import sk.bsmk.clp.domain.CustomerEntity
import java.util.*

data class CustomerDetailDto(
    val id: UUID,
    val name: String,
    val points: Int,
    val tier: LoyaltyTier
) {
    constructor(entity: CustomerEntity) : this(
        id = entity.id.id,
        name = entity.data.name,
        points = entity.data.points,
        tier = entity.data.tier
    )
}