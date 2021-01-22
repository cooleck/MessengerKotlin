package server

import io.ktor.application.*
import io.ktor.sessions.*
import server.data.User

// Модуль, подключающий сессии.
fun Application.sessions() {
    install(Sessions) {
        cookie<User>("User-Cookie", SessionStorageMemory())
    }
}