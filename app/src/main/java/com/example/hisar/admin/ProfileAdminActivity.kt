package com.example.hisar.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.hisar.R
import com.example.hisar.api.ApiClient
import com.example.hisar.data.Data
import com.example.hisar.databinding.ActivityProfileAdminBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileAdminBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val key = intent.getStringExtra("key")
        val agen = intent.getStringExtra("totalAgen")
        val ustad = intent.getStringExtra("totalUstad")
        val jamaah = intent.getStringExtra("totalJamaah")

        binding.totalAgen.text = agen
        binding.totalUstad.text = ustad
        binding.totalJamaah.text = jamaah

        binding.textView2.setOnClickListener{
            onBackPressed()
        }
        binding.imageView2.setOnClickListener {
            onBackPressed()
        }

        getProfile(key)
    }

    private fun getProfile(key: String?){
        ApiClient.apiService.profile(key)
            .enqueue(object : Callback<Data>{
                override fun onResponse(call: Call<Data>, response: Response<Data>) {
                    if (response.isSuccessful){
                        val res= response.body()!!
                        binding.nama.text = res.data[0].nama
                        binding.username.text = res.data[0].username
                    }
                }

                override fun onFailure(call: Call<Data>, t: Throwable) {
                    Toast.makeText(applicationContext,"$t",Toast.LENGTH_LONG).show()
                }

            })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}

