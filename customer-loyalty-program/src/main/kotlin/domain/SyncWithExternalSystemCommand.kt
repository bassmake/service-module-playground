package sk.bsmk.clp.domain

data class SyncWithExternalSystemCommand(
  val getDataCall: () -> ExternalCustomerInfo
)
