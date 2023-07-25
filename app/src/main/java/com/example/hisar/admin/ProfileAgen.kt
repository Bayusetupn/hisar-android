package com.example.hisar.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.hisar.R
import com.example.hisar.data.Data
import com.example.hisar.databinding.ActivityProfileAgenBinding

class ProfileAgen : AppCompatActivity() {
    private lateinit var binding:ActivityProfileAgenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileAgenBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        binding.nama.text = intent.getStringExtra("nama")
        binding.alamat.text = intent.getStringExtra("alamat")
        binding.telp.text = intent.getStringExtra("telp")
        binding.bergabung.text = intent.getStringExtra("bergabung")
        binding.username.text = intent.getStringExtra("username")
        binding.totalJamaah.text = intent.getStringExtra("totalJ")

    }

}