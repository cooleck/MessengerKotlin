package client.data

import kotlinx.serialization.Serializable

@Serializable
data class Chats(val chats: List<Chat>)

@Serializable
data class Chat(val interlocutor: String, val chatId: Int)