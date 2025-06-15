package com.example.socialguard

import android.os.Bundle
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class senstivityactivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_senstivityactivity)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val seekBar = findViewById<SeekBar>(R.id.seekBar)
        val percentageText = findViewById<TextView>(R.id.percentage)

        // Set a listener to update the percentage text dynamically
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                percentageText.text = "$progress%" // Update percentage
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Optional: Handle the event when the user starts dragging the SeekBar
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Optional: Handle the event when the user stops dragging the SeekBar
            }
        })

        val backbtnn = findViewById<ImageView>(R.id.backbtnn)
        backbtnn.setOnClickListener {
            finish()
        }

    }
}