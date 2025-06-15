package com.example.socialguard

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Check if this is the first run
        val preferences: SharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        val isFirstRun = preferences.getBoolean("isFirstRun", true)

        if (isFirstRun) {
            // Redirect to Terms and Conditions activity
            val intent = Intent(this, termsandconditions::class.java)
            startActivity(intent)
            finish()
        } else {
            // Proceed with the usual app flow
            setContentView(R.layout.activity_main)
        }
        
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Set up the bottom navigation listener
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    loadFragment(HomeFragment())  // Load Home Fragment
                    true
                }
                R.id.navigation_alert -> {
                    loadFragment(AlertsFragment()) // Load Alert Fragment
                    true
                }
                R.id.navigation_history -> {
                    loadFragment(HistoryFragment()) // Load History Fragment
                    true
                }
                R.id.navigation_settings -> {
                    loadFragment(SettingsFragment()) // Load Settings Fragment
                    true
                }
                else -> false
            }
        }

        // Load the default fragment when the activity is first created
        if (savedInstanceState == null) {
            loadFragment(HomeFragment()) // Load Home fragment by default
        }
    }

    // Helper function to load fragments
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment) // Replace the fragment container
            .commit()
    }
}
