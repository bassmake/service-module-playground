package sk.bsmk.clp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ClpApp

fun main(args: Array<String>) {
    runApplication<ClpApp>(*args)
}