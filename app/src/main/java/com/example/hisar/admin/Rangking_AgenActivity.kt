package com.example.hisar.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hisar.R
import com.example.hisar.databinding.ActivityRangkingAgenBinding

class Rangking_AgenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRangkingAgenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRangkingAgenBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}