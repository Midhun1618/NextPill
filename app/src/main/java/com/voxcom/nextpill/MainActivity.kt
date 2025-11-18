package com.voxcom.nextpill

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Log.e
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    lateinit var addNewBtn : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets}

        addNewBtn = findViewById(R.id.addNew)

        addNewBtn.setOnClickListener{
            val intent = Intent(this, AddActivity::class.java)
            Log.e("CLick", "addNew button clicked ")
            startActivity(intent)
        }

    }
}