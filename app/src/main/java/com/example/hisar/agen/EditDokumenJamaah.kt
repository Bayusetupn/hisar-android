package com.example.hisar.agen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hisar.R
import com.example.hisar.databinding.ActivityEditDokumenJamaahBinding

class EditDokumenJamaah : AppCompatActivity() {

    private lateinit var binding:ActivityEditDokumenJamaahBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditDokumenJamaahBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}