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

import io.ktor.client.request.*
import io.ktor.client.*

suspend fun authorize(client: HttpClient): String {
    var authStatus = false
    var login = ""
    var password = ""
    while (!authStatus) {
        // Получение авторизационных данных.
        println("Insert your credentials...")
        login = readLine() ?: ""
        password = readLine() ?: ""

        try {
            // Авторизация пользователя.
            val authResponse = client.post<String>("http://127.0.0.1:8080/auth") {
                header("login", login)
                header("password", password)
            }
            if (authResponse == "-1") {
                println("Wrong credentials. Please, try again.")
                continue
            }
            println(authResponse)
            authStatus = true
        }
        catch(e: Exception) {
            println("Wrong credentials. Please, try again.")
        }
    }
    return login
}