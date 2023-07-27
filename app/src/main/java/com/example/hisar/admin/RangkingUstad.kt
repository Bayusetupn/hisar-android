package com.example.hisar.admin

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hisar.DaftarRank
import com.example.hisar.api.ApiClient
import com.example.hisar.data.Data
import com.example.hisar.databinding.ActivityRangkingUstadBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RangkingUstad : AppCompatActivity() {

    private lateinit var binding: ActivityRangkingUstadBinding
    private lateinit var rank:RecyclerView

    private fun getRankAgen(key:String){
        ApiClient.apiService.ustad(key)
            .enqueue(object: Callback<Data>{
                override fun onResponse(call: Call<Data>, response: Response<Data>) {
                    if (response.isSuccessful){
                        val res = response.body()!!
                        val sort: ArrayList<Data.DataAgen> = ArrayList(res.data.sortedByDescending { it.totalJamaah })
                        rank.adapter = DaftarRank(sort,res.data.size)
                        binding.load.visibility = View.GONE
                        binding.daftarRank.visibility = View.VISIBLE

                    }
                }

                override fun onFailure(call: Call<Data>, t: Throwable) {
                    Toast.makeText(applicationContext,"Server Error!",Toast.LENGTH_SHORT).show()
                }

            })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRangkingUstadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val shared = getSharedPreferences("AUTH",Context.MODE_PRIVATE)
        val to:String = shared.getString("KEY",null).toString()

        rank = binding.daftarRank

        rank.layoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL,false)

        getRankAgen(to)

    }
}