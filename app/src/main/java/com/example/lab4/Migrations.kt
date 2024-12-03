package com.example.lab4

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// Міграція з версії 1 до версії 2
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Створюємо таблицю categories
        database.execSQL("""
            CREATE TABLE categories (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                name TEXT NOT NULL
            )
        """.trimIndent())

        // Додаємо колонку categoryId до таблиці data_items
        database.execSQL("""
            ALTER TABLE data_items ADD COLUMN categoryId INTEGER NOT NULL DEFAULT 0 
            REFERENCES categories(id) ON DELETE CASCADE
        """.trimIndent())
    }
}
