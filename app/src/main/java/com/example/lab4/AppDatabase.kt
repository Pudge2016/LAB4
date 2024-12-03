package com.example.lab4
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DataItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dataItemDao(): DataItemDao
}