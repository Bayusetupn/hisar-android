package com.example.hisar.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hisar.R
import com.example.hisar.databinding.ActivityProfileJamaahBinding

class ProfileJamaah : AppCompatActivity() {

    private lateinit var binding: ActivityProfileJamaahBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileJamaahBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nama.text = intent.getStringExtra("nama")
        binding.alamat.text = intent.getStringExtra("alamat")
        binding.ktp.text = intent.getStringExtra("ktp")
        binding.telp.text = intent.getStringExtra("telp")
        binding.kelamin.text = intent.getStringExtra("kelamin")
        binding.gabung.text = intent.getStringExtra("bergabung")
        binding.berangkat.text = if (intent.getStringExtra("berangkat") == null) "Belum ditentukan" else intent.getStringExtra("berangkat")
        binding.paket.text = intent.getStringExtra("paket")
        binding.daftarkan.text = intent.getStringExtra("didaftarkan")
        binding.keterangan.text = if (intent.getBooleanExtra("keterangan",false) == true) "Lunas" else "Belum Lunas"

        binding.textView2.setOnClickListener {
            onBackPressed()
        }
        binding.imageView2.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}