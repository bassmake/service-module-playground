package sk.bsmk.clp.domain.registration

import java.util.*

data class RegistrationCommand(
    val id: UUID,
    val name: String
)