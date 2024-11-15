package de.gally.bonsai

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BonsaiApplication

fun main(args: Array<String>) {
    runApplication<BonsaiApplication>(*args)
}
