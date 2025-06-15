package com.example.socialguard

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Notificationpermission : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_notificationpermission)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val allow_btn = findViewById<Button>(R.id.Allowbutton)
        val notnow_btn = findViewById<Button>(R.id.notnowbutton)


        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Notification Permission Granted", Toast.LENGTH_SHORT).show()
                val intent1 = Intent(this , MainActivity::class.java )
                startActivity(intent1)
            } else {
                Toast.makeText(this, "Turn on Notification later from the settings", Toast.LENGTH_SHORT).show()
                val intent = Intent(this , MainActivity::class.java )
                startActivity(intent)
            }
        }

        allow_btn.setOnClickListener {

            //    requestNotificationPermission()
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED -> {
                    Toast.makeText(this, "Notification Permission Already Granted", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }

        }


        notnow_btn.setOnClickListener {

            Toast.makeText(this, "You can enable notifications later in the app settings.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}