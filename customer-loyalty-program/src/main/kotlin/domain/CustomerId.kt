package sk.bsmk.clp.domain

import java.util.*


inline class CustomerId(val id: UUID) {
    fun str() = id.toString()
}