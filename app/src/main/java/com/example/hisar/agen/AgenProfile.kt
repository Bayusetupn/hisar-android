package com.example.hisar.agen

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hisar.R
import com.example.hisar.api.ApiClient
import com.example.hisar.data.Jamaah
import com.example.hisar.data.RequestId
import com.example.hisar.databinding.ActivityAgenProfileBinding
import com.example.hisar.databinding.ImagePopupBinding
import com.example.hisar.login.DaftarJamaahAdapter_Agen
import com.example.hisar.login.DaftarJamaahAdapter_Self
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AgenProfile : AppCompatActivity() {
    private lateinit var binding: ActivityAgenProfileBinding
    private lateinit var daftar:RecyclerView

    private fun intents(text:String): String? {
        return intent.getStringExtra(text)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgenProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("AUTH",Context.MODE_PRIVATE)
        val to:String = sharedPreferences.getString("KEY",null).toString()

        daftar = binding.daftar
        daftar.layoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL,false)
        binding.textView2.setOnClickListener {
            onBackPressed()
        }
        binding.imageView2.setOnClickListener {
            onBackPressed()
        }
        binding.profilePic.setOnClickListener {
            val dialog = Dialog(this)
            val bind = ImagePopupBinding.inflate(layoutInflater)
            dialog.setContentView(bind.root)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            bind.img.setImageBitmap((binding.profilePic.drawable as BitmapDrawable).bitmap)
            dialog.show()
        }
        Glide
            .with(applicationContext)
            .load("https://api.hisar.my.id/"+ intents("foto"))
            .placeholder(R.drawable.user)
            .into(binding.profilePic)

        binding.nama.text = intents("nama")
        binding.role.text = intents("role")
        binding.alamat.text = intents("alamat")
        binding.ktp.text = intents("ktp")
        binding.username.text = intents("username")
        binding.bergabung.text = intents("bergabung")
        binding.totalJamaah.text = intent.getIntExtra("total",0).toString()
        binding.telp.text = intents("telp")
        getJamaah(to,intents("id").toString())

        binding.edit.setOnClickListener {
            val intent = Intent(applicationContext,EditProfileAgen::class.java)
                .putExtra("id",intents("id"))
                .putExtra("nama",intents("nama"))
                .putExtra("ktp",intents("ktp"))
                .putExtra("username",intents("username"))
                .putExtra("telp",intents("telp"))
                .putExtra("alamat",intents("alamat"))
                .putExtra("role",intents("role"))
            startActivity(intent)
        }

    }

    private fun getJamaah(key:String,id:String){
        ApiClient.apiService.jamaah(key, RequestId(id))
            .enqueue(object: Callback<Jamaah>{
                override fun onResponse(call: Call<Jamaah>, response: Response<Jamaah>) {
                    if (response.isSuccessful){
                        val res = response.body()!!
                        if (res.data.size < 1){
                            binding.load.visibility = View.GONE
                            binding.notFound.visibility = View.VISIBLE
                            daftar.visibility = View.GONE
                        }else{
                            daftar.visibility = View.VISIBLE
                            daftar.adapter = DaftarJamaahAdapter_Self(res.data,2)
                            binding.load.visibility = View.GONE
                            binding.notFound.visibility = View.GONE
                        }
                    }
                }

                override fun onFailure(call: Call<Jamaah>, t: Throwable) {
                    Toast.makeText(applicationContext,"Server Error!",Toast.LENGTH_SHORT).show()
                }

            })
    }
}