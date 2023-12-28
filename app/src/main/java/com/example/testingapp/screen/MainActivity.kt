package com.example.testingapp.screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.testingapp.R
import java.util.Locale

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnCheck = findViewById<Button>(R.id.btnCheck)
        val btnNext = findViewById<Button>(R.id.btnNext)
        val edtName = findViewById<EditText>(R.id.edtName)
        val edtPalindrome = findViewById<EditText>(R.id.edtPalindrome)

        btnCheck.setOnClickListener {
            val userInput = edtPalindrome.text.toString()

            if (isPalindrome(userInput)){
                Toast.makeText(this, "Is Palindrome", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this,"Not Palindrome",Toast.LENGTH_SHORT).show()
            }
        }
        btnNext.setOnClickListener {
            val name = edtName.text.toString()
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("name", name)
            startActivity(intent)
        }
    }

    fun isPalindrome(input: String): Boolean {
        val cleanInput = input.replace("\\s".toRegex(), "").lowercase(Locale.getDefault())
        return cleanInput == cleanInput.reversed()
    }

}