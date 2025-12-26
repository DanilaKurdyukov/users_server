package com.example.features.login

import com.example.database.Users
import com.example.database.user.UserDTO
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureLoginRouting() {

    routing {
        post("/users") {
            // Получаем тело запроса и десериализуем его в объект CreateUserRequest
            val request = call.receive<CreateUserRequest>()

            // Открываем транзакцию Exposed для работы с базой данных
            val userId = transaction {
                // Вставляем нового пользователя в таблицу Users
                Users.insert {
                    it[username] = request.username  // записываем имя пользователя
                    it[email] = request.email        // записываем email (может быть null)
                } get Users.id // получаем id только что вставленной записи
            }
            // Отправляем клиенту ответ 201 Created с JSON, содержащим id нового пользователя
            call.respond(HttpStatusCode.Created, mapOf("id" to userId))
        }
        get("/users") {
            // Открываем транзакцию Exposed для работы с базой данных
            val all = transaction {
                // Получаем все записи из таблицы Users
                Users.selectAll().map { row ->
                    // Преобразуем каждую запись в объект UserDTO
                    UserDTO(
                        id = row[Users.id],            // id пользователя (Int)
                        username = row[Users.username],// имя пользователя (String)
                        email = row[Users.email]       // email пользователя (String?), может быть
                    )
                }
            }
            // Отправляем клиенту результат в виде JSON
            // Здесь используется kotlinx.serialization для сериализации списка UserDTO
            call.respond(all)
        }
    }

}