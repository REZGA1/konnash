package com.example.konnash

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LanguageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language)

        // Find the Arabic button (the one without a clear id name in the layout)
        val btnArabic = findViewById<Button>(R.id.btn) 
        
        btnArabic.setOnClickListener {
            val intent = Intent(this, PhoneNumberActivity::class.java)
            startActivity(intent)
        }
    }
}