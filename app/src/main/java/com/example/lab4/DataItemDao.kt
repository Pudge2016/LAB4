package com.example.lab4
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DataItemDao {
    @Query("SELECT * FROM data_items")
    fun getAllItems(): LiveData<List<DataItem>>

    @Insert
    suspend fun insertItem(item: DataItem)

    @Delete
    suspend fun deleteItem(item: DataItem)

    @Query("DELETE FROM data_items WHERE id = :itemId")
    suspend fun deleteItemById(itemId: Int?)

}