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

package server.database

import com.vladsch.kotlin.jdbc.*
import server.data.Chat

// Класс для работы с базой данных PostgreSQL.
class DataBase {
    companion object {
        // Объект сессии для работы с БД.
        private val session = session("jdbc:postgresql://localhost:5432/messenger", "cooleck", "")

        // Функция авторизации клиента.
        @JvmStatic
        fun authUser(login: String, password: String): Boolean {
            session.first(
                sqlQuery(
                    "select * from users where login = ? and password = ?;",
                    login, password
                )
            ) { row -> row.rowIndex }
                ?: return false

            return true;
        }

        // Функция, регистрирующая нового пользователя.
        @JvmStatic
        fun registerUser(login: String, password: String) {
            session.transaction { transaction ->
                transaction.update(sqlQuery("insert into users (login, password) values(?, ?)", login, password))
            }
        }

        // Функция, получающая список чатов указанного пользователя.
        @JvmStatic
        fun getChats(login: String): List<Chat> {
            val chats: List<Pair<String, Int>> = session.list(
                sqlQuery(
                    "select * " +
                            "from chats where owner1 = :login or owner2 = :login;", mapOf("login" to login)
                )
            ) { row ->
                if (row.string("owner1") == login) {
                    Pair(row.string("owner2"), row.int("chatId"))
                } else {
                    Pair(row.string("owner1"), row.int("chatId"))
                }
            }

            return chats.map { Chat(it.first, it.second) }
        }

        // Функция, проверяющая наличие пользователя с указанным именем.
        @JvmStatic
        fun userExists(username: String): Boolean {
            session.first(
                sqlQuery(
                    "select * from users where login = ?;",
                    username
                )
            ) { row -> row.rowIndex }
                ?: return false

            return true
        }

        // Функция, проверяющая существование чата.
        @JvmStatic
        fun chatExists(owner1: String, owner2: String): Int {
            var chatId = session.first(
                sqlQuery(
                    "select \"chatid\" from chats where owner1 = :owner1 and owner2 = :owner2 or " +
                            "owner1 = :owner2 and owner2 = :owner1",
                    mapOf("owner1" to owner1, "owner2" to owner2)
                )
            ) { row -> row.int("chatid") } ?: -1
            return chatId
        }

        // Функция, создающая новый чат с указанными пользователями owner1 и owner2.
        @JvmStatic
        fun makeChat(owner1: String, owner2: String): Int {
            session.transaction { transaction ->
                transaction.update(
                    sqlQuery(
                        "insert into chats(owner1, owner2) values(?, ?);",
                        owner1,
                        owner2
                    )
                )
            }
            var chatId = session.first(
                sqlQuery(
                    "select \"chatid\" from chats where owner1 = ? " +
                            "and owner2 = ?", owner1, owner2
                )
            ) { row -> row.int("chatid") } ?: -1
            return chatId
        }

        // Функция, доавбляющая сообщение в БД.
        @JvmStatic
        fun addMessage(chatId: Int, sender: String?, message: String) {
            session.transaction { transaction ->
                transaction.update(
                    sqlQuery(
                        "insert into messages(chatid, sender, message) values(:chatId, :sender, :message);",
                        mapOf("chatId" to chatId, "sender" to sender, "message" to message)
                    )
                )
            }
        }

        // Функция получающая историю сообщений в чате.
        @JvmStatic
        fun getChatHistory(chatId: Int): List<Pair<String, String>> {
            val chatHistory: List<Pair<String, String>> = session.list(
                sqlQuery(
                    "select sender, message from messages where chatid = :chatId;",
                    mapOf("chatId" to chatId)
                )
            ) { row -> Pair(row.string("sender"), row.string("message")) }

            return chatHistory
        }
    }
}