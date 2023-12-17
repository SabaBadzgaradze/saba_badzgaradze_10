package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startServiceButton = findViewById<View>(R.id.startServiceButton)
        startServiceButton.setOnClickListener {
            val serviceIntent = Intent(this, MyService::class.java)
            startService(serviceIntent)
        }
    }
}
