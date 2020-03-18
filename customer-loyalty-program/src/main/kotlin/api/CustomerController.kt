package sk.bsmk.clp.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sk.bsmk.clp.shared.LoyaltyTier
import java.util.*

@RestController
@RequestMapping("/customers")
class CustomerController {

    @GetMapping("/{id}")
    fun customerDetail(@PathVariable id: UUID): CustomerDetailDto {
        return CustomerDetailDto(
            id = id,
            name = "TODO",
            points = 0,
            tier = LoyaltyTier.NONE
        )
    }

}