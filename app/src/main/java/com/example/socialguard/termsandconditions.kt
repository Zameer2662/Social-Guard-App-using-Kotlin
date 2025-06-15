package com.example.socialguard

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class termsandconditions : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_termsandconditions)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val accept_btn = findViewById<Button>(R.id.acceptbutton)
        val decline_btn = findViewById<Button>(R.id.declinebutton)

        accept_btn.setOnClickListener {
            // Set `isFirstRun` to `false` and proceed
            val preferences: SharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putBoolean("isFirstRun", false)
            editor.apply()

            // Navigate to the next screen
            val intent = Intent(this, Notificationpermission::class.java)
            startActivity(intent)
            finish()
        }

        decline_btn.setOnClickListener {
            // Set `isFirstRun` to `true`, then close the app
            val preferences: SharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putBoolean("isFirstRun", true)
            editor.apply()

            // Close the app
            finishAffinity() // Ensures the app fully exits
        }
    }

    // When the back button is pressed, set `isFirstRun` to `true` and close
    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        super.onBackPressed()
        val preferences: SharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean("isFirstRun", true)
        editor.apply()
        finishAffinity()
    }
}
