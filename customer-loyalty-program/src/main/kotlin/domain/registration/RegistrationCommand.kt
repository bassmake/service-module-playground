package sk.bsmk.clp.domain.registration

import sk.bsmk.clp.domain.CustomerId

data class RegistrationCommand(
    val id: CustomerId,
    val name: String
)