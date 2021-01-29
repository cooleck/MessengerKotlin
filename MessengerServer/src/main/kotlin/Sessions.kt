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
import io.ktor.sessions.*
import server.data.User

// Модуль, подключающий сессии.
fun Application.sessions() {
    install(Sessions) {
        cookie<User>("User-Cookie", SessionStorageMemory())
    }
}