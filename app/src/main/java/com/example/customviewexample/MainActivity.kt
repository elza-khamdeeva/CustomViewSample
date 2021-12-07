package com.example.customviewexample

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.customviewexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initCounter()
    }

    private fun initCounter() {
        with(binding) {
            incrementButton.setOnClickListener {
                counterView.increment()
                counterView.circleColor = Color.RED
            }
            resetButton.setOnClickListener {
                counterView.reset()
            }
        }
    }
}
