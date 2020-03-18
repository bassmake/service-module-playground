package sk.bsmk.clp.api

import sk.bsmk.clp.shared.LoyaltyTier
import java.util.*

data class CustomerDetailDto(
    val id: UUID,
    val name: String,
    val points: Int,
    val tier: LoyaltyTier
)