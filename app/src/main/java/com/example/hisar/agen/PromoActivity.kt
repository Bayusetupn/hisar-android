package com.example.hisar.agen

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hisar.PromoAdapter
import com.example.hisar.R
import com.example.hisar.SlideAdapter
import com.example.hisar.api.ApiClient
import com.example.hisar.data.Banner
import com.example.hisar.databinding.ActivityPromoBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PromoActivity : AppCompatActivity() {

    private lateinit var binding:ActivityPromoBinding
    private lateinit var promo:RecyclerView
    private lateinit var adapter:PromoAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("AUTH",Context.MODE_PRIVATE)
        val to:String = sharedPreferences.getString("KEY",null).toString()

        binding = ActivityPromoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        promo = binding.listPromo
        promo.layoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL,false)
        promo(to)

    }
    private fun promo(key:String){
        ApiClient.apiService.getPromo(key)
            .enqueue(object: Callback<Banner> {
                override fun onResponse(call: Call<Banner>, response: Response<Banner>) {
                    if (response.isSuccessful){
                        val res = response.body()!!
                        adapter = PromoAdapter(res.data)
                        promo.adapter = adapter
                    }
                }

                override fun onFailure(call: Call<Banner>, t: Throwable) {
                    Toast.makeText(applicationContext,"Server Error!", Toast.LENGTH_SHORT).show()
                }

            })
    }
}