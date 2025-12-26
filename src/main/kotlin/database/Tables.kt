package com.example.database

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import java.util.UUID

// 1) Автоинкрементный integer primary key
object Users : Table("users") {
    val id = integer("id").autoIncrement()         // INT AUTO_INCREMENT
    val username = varchar("username", 50).uniqueIndex()
    val email = varchar("email", 255).nullable()

    override val primaryKey = PrimaryKey(id) // явно, но autoIncrement уже даёт PK
}

// 2) UUID primary key (если хочешь распределённые id)
object Sessions : Table("sessions") {
    val id = uuid("id").clientDefault { UUID.randomUUID() } // UUID default
    val userId = reference("user_id", Users.id, onDelete = ReferenceOption.CASCADE)
    val token = varchar("token", 128).uniqueIndex()

    override val primaryKey = PrimaryKey(id)
}

// 3) Composite primary key (например, связь many-to-many)
object UserRoles : Table("user_roles") {
    val userId = reference("user_id", Users.id, onDelete = ReferenceOption.CASCADE)
    val role = varchar("role", 50)

    override val primaryKey = PrimaryKey(userId, role, name = "PK_UserRole")
}

// 4) Таблица с внешним ключом и разными типами колонок
object Orders : Table("orders") {
    val id = long("id").autoIncrement() // long для больших чисел
    val userId = reference("user_id", Users.id)
    val total = decimal("total", precision = 10, scale = 2).default(0.toBigDecimal())
    val status = varchar("status", 20).default("NEW") // можно хранить enum как string
    val notes = text("notes").nullable()

    override val primaryKey = PrimaryKey(id)
}

// 5) Пример enum-like с проверкой (можно хранить int или string)
enum class CandyType { CHOCOLATE, JELLY, CARAMEL }
object Candies : Table("candies") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 100)
    val type = varchar("type", 20).default(CandyType.CHOCOLATE.name) // store enum name
    override val primaryKey = PrimaryKey(id)
}