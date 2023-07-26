package com.example.hisar.admin

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.hisar.R
import com.example.hisar.api.ApiClient
import com.example.hisar.data.Perkab
import com.example.hisar.data.PerkabJamaah
import com.example.hisar.data.RequestId
import com.example.hisar.databinding.ActivityProfileJamaahBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileJamaah : AppCompatActivity() {

    private lateinit var binding: ActivityProfileJamaahBinding

    private fun getPerkab(key: String?,id: String){
        ApiClient.apiService.perkab(key, RequestId(id))
            .enqueue(object: Callback<PerkabJamaah>{
                override fun onResponse(
                    call: Call<PerkabJamaah>,
                    response: Response<PerkabJamaah>
                ) {
                    if (response.isSuccessful) {
                        val res = response.body()!!
                        val cek = res.data
                        binding.koper.isChecked = cek.koperDll
                        binding.batik.isChecked = cek.kainBatik
                        binding.ihram.isChecked = cek.kainIhram
                        binding.mukena.isChecked = cek.mukena
                        binding.syal.isChecked = cek.syal
                        binding.panduan.isChecked = cek.bukuPanduan
                        binding.doa.isChecked = cek.bukuDoa
                        binding.sholawat.isChecked = cek.bookletSholawat
                        binding.peta.isChecked = cek.bookletPeta

                    }
                }

                override fun onFailure(call: Call<PerkabJamaah>, t: Throwable) {
                    Toast.makeText(applicationContext,"${t.message}",Toast.LENGTH_SHORT).show()
                }

            })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileJamaahBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("AUTH", Context.MODE_PRIVATE)
        val to:String = sharedPreferences.getString("KEY",null).toString()

        intent.getStringExtra("id")?.let { getPerkab(to, it) }

        //profile
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

        //back
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