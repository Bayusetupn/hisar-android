package com.example.hisar.admin

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hisar.RiwayatLoginAdapter
import com.example.hisar.api.ApiClient
import com.example.hisar.data.Jamaah
import com.example.hisar.data.RequestId
import com.example.hisar.data.RiwayatLogin
import com.example.hisar.databinding.ActivityProfileAgenBinding
import com.example.hisar.login.DaftarJamaahAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class ProfileAgen : AppCompatActivity() {
    private lateinit var binding:ActivityProfileAgenBinding
    private lateinit var daftar:RecyclerView
    private lateinit var riwayat:RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileAgenBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun getRiwayat(key: String?,id: String?){
        ApiClient.apiService.riwayatLogin(key,RequestId(id))
            .enqueue(object: Callback<RiwayatLogin>{
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(
                    call: Call<RiwayatLogin>,
                    response: Response<RiwayatLogin>
                ) {
                    if (response.isSuccessful){
                        val res = response.body()!!
                        val date = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        val sort: ArrayList<RiwayatLogin.Login> = ArrayList(res.data.sortedByDescending { Instant.parse(it.login).atZone(
                            ZoneId.of("Asia/Jakarta")).format(date) })
                        riwayat.adapter = RiwayatLoginAdapter(sort)
                    }
                }

                override fun onFailure(call: Call<RiwayatLogin>, t: Throwable) {
                    Toast.makeText(applicationContext,"Server Error!",Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun getJamaah(key:String?,id:String?){
            ApiClient.apiService.jamaah(key, RequestId(id))
                .enqueue(object : Callback<Jamaah>{
                    override fun onResponse(call: Call<Jamaah>, response: Response<Jamaah>) {
                        if (response.isSuccessful){
                            val res = response.body()!!
                            daftar.adapter = DaftarJamaahAdapter(res.data,3)
                            binding.load.visibility = View.GONE
                            if (res.data.size == 0){
                                binding.notFound.visibility = View.VISIBLE
                            }else{
                                binding.daftar.visibility = View.VISIBLE
                            }
                        }
                    }

                    override fun onFailure(call: Call<Jamaah>, t: Throwable) {
                        Toast.makeText(applicationContext,"Server Error!",Toast.LENGTH_SHORT).show()
                    }

                })
    }

    override fun onStart() {
        super.onStart()
        daftar = binding.daftar
        riwayat = binding.riwayat

        riwayat.layoutManager = object : LinearLayoutManager(applicationContext){
            override fun canScrollVertically(): Boolean {
                return false
            }

            override fun canScrollHorizontally(): Boolean {
                return false
            }
        }

        daftar.layoutManager = object : LinearLayoutManager(applicationContext){
            override fun canScrollVertically(): Boolean {
                return false
            }

            override fun canScrollHorizontally(): Boolean {
                return false
            }
        }
        val sharedPreferences: SharedPreferences = getSharedPreferences("AUTH", Context.MODE_PRIVATE)
        val to:String = sharedPreferences.getString("KEY",null).toString()
        val id = intent.getStringExtra("id")
        getJamaah(to,id)
        getRiwayat(to,id)

        binding.textView2.setOnClickListener{
            onBackPressed()
        }
        binding.imageView2.setOnClickListener{
            onBackPressed()
        }
        binding.role.text = intent.getStringExtra("role")
        binding.nama.text = intent.getStringExtra("nama")
        binding.alamat.text = intent.getStringExtra("alamat")
        binding.telp.text = intent.getStringExtra("telp")
        binding.bergabung.text = intent.getStringExtra("bergabung")
        binding.username.text = intent.getStringExtra("username")
        binding.totalJamaah.text = intent.getStringExtra("totalJ")
        binding.ktp.text = intent.getStringExtra("ktp")

        val idAgen = intent.getStringExtra("id")

        binding.more.setOnClickListener {
            val intent = Intent(applicationContext,DaftarJamaah::class.java)
                .putExtra("id",idAgen)
                .putExtra("key",to)
            startActivity(intent)
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}