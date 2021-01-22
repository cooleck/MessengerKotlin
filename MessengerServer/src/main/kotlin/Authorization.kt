package server

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import server.database.DataBase
import server.data.User

// Модуль для авторизации и регистрации пользователей.
fun Application.authorization() {
    routing {
        post("/auth") {
            val login = call.request.headers["login"] ?: ""
            val password = call.request.headers["password"] ?: ""

            call.sessions.set(User(login))

            if (DataBase.userExists(login)) {
                if (DataBase.authUser(login, password)) {
                    call.respondText("Welcome back, $login!")
                } else {
                    call.respond(-1)
                }
            }
            else {
                DataBase.registerUser(login, password)
                call.respondText("User $login successfully registered!")
            }
        }
    }
}