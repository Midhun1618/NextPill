package com.voxcom.nextpill

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    lateinit var addNewBtn : Button
    lateinit var editListBtn : Button
    lateinit var userName: TextView
    lateinit var userAge : TextView
    lateinit var imgBtn : ImageButton
    lateinit var fileBtn : ImageButton
    lateinit var customBtn : ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imgBtn =findViewById(R.id.btnImg)
        fileBtn =findViewById(R.id.btnFile)
        customBtn =findViewById(R.id.btnCustom)

        addNewBtn = findViewById(R.id.addNew)

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.addnew_dialog)
        dialog.setCancelable(true)

        addNewBtn.setOnClickListener{
            dialog.show()
        }

        customBtn.setOnClickListener {

        }

    }
}