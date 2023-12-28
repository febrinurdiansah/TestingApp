package com.example.testingapp.screen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.testingapp.R

class SecondActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val name = intent.getStringExtra("name")
        val tvName = findViewById<TextView>(R.id.tvUserName)
        tvName.text = name

        val btnBack = findViewById<ImageView>(R.id.imgBackSecond)
        btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val btnChoose = findViewById<Button>(R.id.btnChoose)
        btnChoose.setOnClickListener {
            val intent = Intent(this, ThirdActivity::class.java)
            startActivity(intent)
        }
        if (getNameFromSharedPreferences() != null) {
            val textViewName = findViewById<TextView>(R.id.tvUserName)
            textViewName.text = name
        }
        val intent = intent
        val firstName = intent.getStringExtra("first_name") ?: ""
        val lastName = intent.getStringExtra("last_name") ?: ""
        val fullName = if (firstName.isNotEmpty() && lastName.isNotEmpty()) {
            "$firstName $lastName"
        } else {
            "Selected User Name"
        }
        findViewById<TextView>(R.id.tvSelectedUsername).text = fullName
    }

    private fun getNameFromSharedPreferences(): String? {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("name", null)
    }
}