package client

import client.data.Chats
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.cio.websocket.*
import io.ktor.client.*

suspend fun DefaultClientWebSocketSession.outputMessages() {
    try {
        for (message in incoming) {
            message as? Frame.Text ?: continue
            println(message.readText())

        }
    } catch (e: Exception) {
        error("Got error: " + e.localizedMessage)
    }
}

suspend fun DefaultClientWebSocketSession.inputMessages() {
    println("Чат:")
    while (true) {
        val message = readLine() ?: ""
        if (message.equals("/exit")) {
            return
        }

        try {
            outgoing.send(Frame.Text(message))
        } catch (e: Exception) {
            println("Error occurred while trying to send your message(")
        }
    }
}

suspend fun chooseChat(client: HttpClient): Int {
    // Получение существующих чатов у данного пользователя.
    val chatsResponse = client.get<Chats>("http://127.0.0.1:8080/chats")
    if (chatsResponse.chats.isEmpty()) {
        println("You don't have any chats.")
    } else {
        println("Users with existing chats:")
        chatsResponse.chats.forEach { println("\t${it.interlocutor}") }
    }

    var chatId = -1
    while (chatId == -1) {
        println("\nEnter the name of your interlocutor:")

        // Подключение к чату с указанным пользователем.
        val interlocutor = readLine() ?: ""
        if (interlocutor == "/exit") break

        // Отправляем запрос на создание нового чата и подключение к существующему.
        chatId = client.get<String>("http://127.0.0.1:8080/makeChat") {
            header("interlocutor", interlocutor)
        }.toInt()

        if (chatId == -1) {
            println("User doesn't exist(")
            continue
        } else break
    }

    return chatId
}