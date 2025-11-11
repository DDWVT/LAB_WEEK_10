package com.example.lab_week_10

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.lab_week_10.database.Total
import com.example.lab_week_10.database.TotalDatabase
import com.example.lab_week_10.database.TotalObject
import com.example.lab_week_10.viewmodels.TotalViewModel
import java.util.Date

class MainActivity: AppCompatActivity() {
    private val db by lazy { prepareDatabase() }
    private val viewModel by lazy {
        ViewModelProvider(this)[TotalViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeValueFromDatabase()
        prepareViewModel()
    }

    override fun onStart() {
        super.onStart()
        val total = db.totalDao().getTotal(ID)
        if (total.isNotEmpty() && total.first().total.date.isNotBlank()) {
            Toast.makeText(this, total.first().total.date, Toast.LENGTH_LONG).show()
        }
    }

    override fun onPause() {
        super.onPause()
        val currentDate = Date().toString()
        val currentValue = viewModel.total.value?.value ?: 0
        val newTotalObject = TotalObject(value = currentValue, date = currentDate)
        db.totalDao().update(Total(ID, newTotalObject))
    }

    private fun prepareDatabase(): TotalDatabase {
        return Room.databaseBuilder(
            applicationContext,
            TotalDatabase::class.java, "total-database"
        ).allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    private fun initializeValueFromDatabase() {
        val total = db.totalDao().getTotal(ID)
        if (total.isEmpty()) {
            val initialObject = TotalObject(value = 0, date = "N/A")
            db.totalDao().insert(Total(id = 1, total = initialObject))
            viewModel.setTotal(initialObject)
        } else {
            viewModel.setTotal(total.first().total)
        }
    }

    private fun prepareViewModel(){
        viewModel.total.observe(this, {
            updateText(it.value)
        })

        findViewById<Button>(R.id.button_increment).setOnClickListener {
            viewModel.incrementTotal()
        }
    }

    private fun updateText(total: Int) {
        findViewById<TextView>(R.id.text_total).text =
            getString(R.string.text_total, total)
    }

    companion object {
        const val ID: Long = 1
    }
}