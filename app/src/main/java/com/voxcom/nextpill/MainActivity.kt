package com.voxcom.nextpill

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    lateinit var addNewBtn: Button
    lateinit var adapter: MedicineAdapter
    lateinit var medList: MutableList<MedicineInfo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        adapter = MedicineAdapter(medList)
        val recyclerView = findViewById<RecyclerView>(R.id.listView)
        addNewBtn = findViewById(R.id.addNew)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        addNewBtn.setOnClickListener {
            Log.e("Click", "addNew button clicked")
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }
    }
}
