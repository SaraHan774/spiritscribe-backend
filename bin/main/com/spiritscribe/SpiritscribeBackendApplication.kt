package com.spiritscribe

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.spiritscribe"])
class SpiritscribeBackendApplication

fun main(args: Array<String>) {
    runApplication<SpiritscribeBackendApplication>(*args)
}


