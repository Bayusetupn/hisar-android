package com.example.hisar.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hisar.R
import com.example.hisar.databinding.ActivityProfileAdminBinding

class ProfileAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}