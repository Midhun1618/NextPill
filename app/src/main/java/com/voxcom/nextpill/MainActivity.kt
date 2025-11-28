package com.voxcom.nextpill

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    private lateinit var addNewBtn: Button
    private lateinit var adapter: MedicineAdapter
    private lateinit var medList: MutableList<MedicineInfo>
    private lateinit var dateMon: TextView
    private lateinit var viewModel: MedicineViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dateMon = findViewById(R.id.date_mon)
        val formatter = DateTimeFormatter.ofPattern("dd MMM")
        dateMon.text = LocalDate.now().format(formatter)

        viewModel = MedicineViewModel(application)

        medList = mutableListOf()
        adapter = MedicineAdapter(medList)

        val recyclerView = findViewById<RecyclerView>(R.id.listView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.medicines.collect { list ->
                medList.clear()
                medList.addAll(list)
                adapter.notifyDataSetChanged()
            }
        }

        addNewBtn = findViewById(R.id.addNew)
        addNewBtn.setOnClickListener {
            Log.e("Click", "addNew button clicked")
            startActivity(Intent(this, AddActivity::class.java))
        }
    }
}
