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

package client

import io.ktor.client.*
import io.ktor.client.features.cookies.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.*


// Функция, реализующая клиент мессенджера.
fun main(args: Array<String>) {
    // Создание инстанса клиента.
    val client = HttpClient {
        install(WebSockets)
        install(HttpCookies) {
            storage = AcceptAllCookiesStorage()
        }
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }


    runBlocking {
        // Авторизация клиента.
        val login = authorize(client)

        while (true) {
            // Выбор чата.
            var chatId = chooseChat(client)
            if (chatId == -1) break

            // Подключаемся к чату.
            client.webSocket(
                method = HttpMethod.Get,
                host = "127.0.0.1",
                port = 8080,
                path = "/chat",
                request = {
                    header("chatId", chatId)
                    header("user", login)
                }
            ) {
                // Запускаем два параллельных процесса чтения и отправки сообщений.
                val messageOutputRoutine = launch { outputMessages() }
                val userInputRoutine = launch { inputMessages() }
                userInputRoutine.join()
                messageOutputRoutine.cancelAndJoin()
            }
        }

        client.close()
        println("Connection closed.")
    }
    return
}