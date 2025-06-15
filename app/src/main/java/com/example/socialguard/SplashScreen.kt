package com.example.socialguard

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper


class SplashScreen : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)




        Handler(Looper.getMainLooper()).postDelayed(
            {
                auth = FirebaseAuth.getInstance()
                sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

                // Check if it's the first run
                val isFirstRun = sharedPreferences.getBoolean("isFirstRun", true)

                if (isFirstRun) {
                    // Show login activity if it's the first run
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("isFirstRun", false)
                    editor.apply()
                    val intent = Intent(this, SigninActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Check if the user is already signed in
                    val currentUser: FirebaseUser? = auth.currentUser
                    if (currentUser != null) {
                        // User is signed in, redirect to MainActivity
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // No user is signed in, redirect to SignInActivity
                        val intent = Intent(this, SignupActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }


            },1500
        )





    }
}


