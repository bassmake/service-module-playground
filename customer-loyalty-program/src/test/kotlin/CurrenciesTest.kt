package sk.bsmk.clp

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import sk.bsmk.clp.domain.transactions.Currencies
import java.util.*
import java.util.stream.Stream

class CurrenciesTest {

    @ParameterizedTest
    @MethodSource("currencies")
    fun `currency can be parsed`(str: String, expected: Currency) {
        val currency = Currencies.parse(str)
        Assertions.assertEquals(expected, currency)
    }

    fun currencies(): Stream<Arguments> {
        return Stream.of(
            Arguments.arguments("EUR", Currencies.EUR),
            Arguments.arguments("eur", Currencies.EUR),
            Arguments.arguments("USD", Currencies.USD)
        )
    }

}