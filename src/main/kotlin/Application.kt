package com.example

import com.example.database.DatabaseConfig
import com.example.features.login.configureLoginRouting
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {


    DatabaseConfig.init()
    configureLoginRouting() // POST и GET
    configureSerialization()
}
