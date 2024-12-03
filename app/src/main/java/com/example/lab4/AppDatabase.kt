package com.example.lab4
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DataItem::class, Category::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dataItemDao(): DataItemDao
    abstract fun categoryDao(): CategoryDao
}