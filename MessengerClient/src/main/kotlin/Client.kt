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