package server

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*

fun main(args: Array<String>) = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    install(ContentNegotiation) {
        gson()
    }
}