package com.example.lab4

import android.os.Bundle
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

    private lateinit var adapter: RecyclerAdapter
    private lateinit var dataItemDao: DataItemDao
    private lateinit var dataList: LiveData<List<DataItem>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ініціалізація бази даних
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "data-items-db"
        ).build()
        dataItemDao = db.dataItemDao()

        // В ініціалізації RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

// Ініціалізація адаптера без передавання списку
        val adapter = RecyclerAdapter()

        recyclerView.adapter = adapter

// Спостереження за LiveData з бази даних
        dataList = dataItemDao.getAllItems()
        dataList.observe(this, Observer { updatedList ->
            adapter.submitList(updatedList) // Оновлюємо адаптер новими даними
        })

        // Додавання елементів
        findViewById<Button>(R.id.addButton).setOnClickListener {
            val newItem = DataItem(title = "New Item", description = "New Description")
            CoroutineScope(Dispatchers.IO).launch {
                dataItemDao.insertItem(newItem)
            }
        }

        // Видалення останнього елемента
        findViewById<Button>(R.id.deleteButton).setOnClickListener {
            val currentList = dataList.value
            if (currentList != null && currentList.isNotEmpty()) {
                val lastItem = currentList.last()
                CoroutineScope(Dispatchers.IO).launch {
                    dataItemDao.deleteItem(lastItem)
                }
            }
        }
    }
}
