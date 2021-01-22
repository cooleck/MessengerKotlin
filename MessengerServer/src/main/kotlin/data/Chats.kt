package server.data

import io.ktor.locations.*
import kotlinx.serialization.Serializable

// Класс списка чатов для предоставления клиенту.
@Serializable
data class Chats(val chats: List<Chat>)

// Класс одного чата.
@Serializable
data class Chat(val interlocutor: String, val chatId: Int)
