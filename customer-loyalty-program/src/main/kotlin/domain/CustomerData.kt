package sk.bsmk.clp.domain

import sk.bsmk.clp.shared.LoyaltyTier


data class CustomerData(
    val name: String,
    val tier: LoyaltyTier,
    val points: Int
) {
    constructor(name: String) : this(
        name = name,
        tier = LoyaltyTier.NONE,
        points = 0
    )
}