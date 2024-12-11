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

    @Query("DELETE FROM data_items WHERE categoryId = :categoryId")
    suspend fun deleteItemsByCategoryId(categoryId: Int)
}
@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories")
    fun getAllCategories(): LiveData<List<Category>>

    @Insert
    suspend fun insertCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Query("DELETE FROM categories WHERE id = :categoryId")
    suspend fun deleteCategoryById(categoryId: Int)

    @Query("SELECT * FROM categories WHERE id = :categoryId")
    suspend fun getCategoryById(categoryId: Int): Category?
}
