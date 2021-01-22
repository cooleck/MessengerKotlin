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