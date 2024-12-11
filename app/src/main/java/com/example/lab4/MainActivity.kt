package com.example.lab4

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var categoryList: List<Category>  // Список категорій
    private lateinit var adapter: RecyclerAdapter
    private lateinit var dataItemDao: DataItemDao
    private lateinit var categoryDao: CategoryDao
    private lateinit var dataList: LiveData<List<DataItem>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ініціалізація бази даних
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "app-database"
        ).build()

        dataItemDao = db.dataItemDao() // Ініціалізуємо DAO для елементів
        categoryDao = db.categoryDao() // Ініціалізуємо DAO для категорій

        // В ініціалізації RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = RecyclerAdapter(onDeleteClick = { item ->
            CoroutineScope(Dispatchers.IO).launch {
                dataItemDao.deleteItem(item)
            }
        }, categoryDao = categoryDao)  // Pass the categoryDao to the adapter
        recyclerView.adapter = adapter

        // Спостереження за LiveData з бази даних
        dataList = dataItemDao.getAllItems()
        dataList.observe(this, Observer { updatedList ->
            adapter.submitList(updatedList) // Оновлюємо адаптер новими даними
        })

        // Спостереження за категоріями
        db.categoryDao().getAllCategories().observe(this, Observer { categories ->
            if (!categories.isNullOrEmpty()) {
                categoryList = categories  // Зберігаємо категорії для подальшого використання
            } else {
                Log.d("MainActivity", "Категорії відсутні")
            }
        })

        // Додавання елементу
        findViewById<Button>(R.id.addButton).setOnClickListener {
            if (categoryList.isNotEmpty()) {
                val categoryId = categoryList.last().id  // Використовуємо останню категорію
                val newItem = DataItem(title = "New Item", description = "New Description", categoryId = categoryId)

                // Додаємо елемент у базу даних
                CoroutineScope(Dispatchers.IO).launch {
                    dataItemDao.insertItem(newItem)
                }
            } else {
                Log.d("MainActivity", "Категорії відсутні для додавання елемента")
            }
        }

        // Додавання нової категорії
        findViewById<Button>(R.id.addCategoryButton).setOnClickListener {
            // Додати нову категорію
            CoroutineScope(Dispatchers.IO).launch {
                val newCategory = Category(name = "New Category")
                categoryDao.insertCategory(newCategory) // Додати категорію

                // Після додавання категорії, оновимо список категорій
                updateCategoryList()
            }
        }
        findViewById<Button>(R.id.deleteCategoryButton).setOnClickListener {
            if (categoryList.isNotEmpty()) {
                val categoryIdToDelete = categoryList.last().id  // Або оберіть категорію, яку хочете видалити

                CoroutineScope(Dispatchers.IO).launch {
                    // Спочатку видаляємо всі елементи цієї категорії
                    dataItemDao.deleteItemsByCategoryId(categoryIdToDelete)

                    // Потім видаляємо саму категорію
                    categoryDao.deleteCategoryById(categoryIdToDelete)
                }
            } else {
                Log.d("MainActivity", "Немає категорій для видалення")
            }
        }

        // Видалення останнього елемента

    }

    // Метод для оновлення списку категорій після додавання нової категорії
    private fun updateCategoryList() {
        CoroutineScope(Dispatchers.Main).launch {
            val categories = categoryDao.getAllCategories().value
            if (!categories.isNullOrEmpty()) {
                categoryList = categories
                Log.d("MainActivity", "Список категорій оновлено")
            }
        }
    }

}
