/**
 * KotlinMessenger - client/server software for communication.
 *
 * Copyright 2021 by Nikulin Ivan ianikulin@edu.hse.ru
 */

/**
 * This file is part of MessengerKotlin.
 *
 * MessengerKotlin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MessengerKotlin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MessengerKotlin.  If not, see <https://www.gnu.org/licenses/>.
 * package server
 */

package server

import io.ktor.application.*
import io.ktor.http.cio.websocket.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.websocket.*
import server.database.DataBase
import server.data.User
import server.data.Chats
import kotlinx.coroutines.runBlocking
import java.lang.Exception
import java.util.*
import kotlin.collections.LinkedHashSet

// Модуль, реализующий функционал с чатами.
fun Application.chats() {
    install(WebSockets)

    routing {
        get("/chats") {
            val user = call.sessions.get<User>() ?: error("No cookies founded(")
            runBlocking {
                val chats = DataBase.getChats(user.login)
                call.respond(Chats(chats))
            }
        }

        get("/makeChat") {
            val user = call.sessions.get<User>() ?: error("No cookies founded(")
            val interlocutor = call.request.headers["interlocutor"] ?: ""
            runBlocking {
                if (!DataBase.userExists(interlocutor)) {
                    call.respondText("-1")
                } else {
                    var chatId = DataBase.chatExists(user.login, interlocutor)
                    if (chatId == -1) {
                        chatId = DataBase.makeChat(user.login, interlocutor)
                    }
                    call.respondText("$chatId")
                }
            }
        }

        // Множество соединенений с клиентами.
        val connections = Collections.synchronizedSet(LinkedHashSet<Pair<Int, DefaultWebSocketSession?>>())

        // Хэндлер обработки сообщений в чате.
        webSocket("/chat") {
            // Получаем логин и chatId чата.
            val chatId = call.request.headers["chatId"]!!.toInt()
            val user = call.request.headers["user"]

            // Добавляем клиента с множество соединений и возвращаем истрорию сообщений чата.
            connections.add(Pair<Int, DefaultWebSocketSession>(chatId, this))
            val history = DataBase.getChatHistory(chatId)
            history.forEach { this.send("User ${it.first}: ${it.second}") }
            try {
                for (message in incoming) {
                    message as? Frame.Text ?: continue
                    val receivedMessage = message.readText()
                    connections.filter { it.first == chatId }
                        .forEach { it.second?.send("User $user: $receivedMessage") }
                    // Добавляем сообщение в БД.
                    DataBase.addMessage(chatId, user, receivedMessage)
                }
            }
            catch(e: Exception) {
                this.send("Error raises while parsing your message(")
            }
            finally {
                connections -= Pair(chatId, this)
            }


        }
    }
}