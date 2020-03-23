package sk.bsmk.clp.domain.transactions

import java.util.*


object Currencies {

    val EUR: Currency = Currency.getInstance(Locale.GERMANY)
    val USD: Currency = Currency.getInstance(Locale.US)

    fun parse(value: String): Currency? {
        return when (value.toUpperCase()) {
            "EUR" -> EUR
            "USD" -> USD
            else -> null
        }
    }

}