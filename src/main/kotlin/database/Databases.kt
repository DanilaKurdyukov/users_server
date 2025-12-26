package com.example.database

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseConfig {

    fun init(): Boolean {
        return try {
            Database.connect(
                url = "jdbc:postgresql://gondola.proxy.rlwy.net:35579/railway",
                // JDBC-ссылка к БД Railway (домен caboose.proxy.rlwy.net, порт 19083)
                // Данные берутся из переменных окружения RAILWAY_TCP_PROXY_DOMAIN и RAILWAY_TCP_PROXY_PORT
                driver = "org.postgresql.Driver", // драйвер PostgreSQL
                user = "postgres", // PGUSER -> postgres
                password = "HlbFUVDFFtoZsoNVuphFkdbEpCUtXcbv" // пароль из PGPASSWORD
            )

            transaction {
                // Здесь можно создавать или инициализировать таблицы
                SchemaUtils.create(Users)
            }

            println("✅ Подключение к БД успешно!")
            true
        } catch (e: Exception) {
            println("❌ Ошибка подключения к БД: ${e.message}")
            false
        }
    }
}
