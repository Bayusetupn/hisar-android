package com.example.hisar.admin

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hisar.api.ApiClient
import com.example.hisar.data.Jamaah
import com.example.hisar.data.RequestId
import com.example.hisar.databinding.ActivityDaftarJamaahAgenBinding
import com.example.hisar.databinding.ActivityDaftarJamaahBinding
import com.example.hisar.login.DaftarJamaahAdapter
import com.example.hisar.login.DaftarJamaahAdapter_Agen
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class DaftarJamaah_Agen : AppCompatActivity() {

    private lateinit var binding: ActivityDaftarJamaahAgenBinding
    private lateinit var daftar:RecyclerView
    private lateinit var adapter: DaftarJamaahAdapter_Agen
    private lateinit var list:ArrayList<Jamaah.DataItem>

    private fun getDataJamaah(key:String?, id:String?){
        ApiClient.apiService.jamaah(key, RequestId(id))
            .enqueue(object : Callback<Jamaah>{
                override fun onResponse(call: Call<Jamaah>, response: Response<Jamaah>) {
                    if (response.isSuccessful){
                        val res = response.body()!!
                        binding.load.visibility = View.GONE
                        binding.daftar.visibility = View.VISIBLE
                        adapter = DaftarJamaahAdapter_Agen(res.data,res.data.size)
                        list = res.data
                        daftar.adapter = adapter
                    }
                }

                override fun onFailure(call: Call<Jamaah>, t: Throwable) {
                    Toast.makeText(applicationContext,"Server Error!", Toast.LENGTH_SHORT).show()
                }

            })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarJamaahAgenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(query: Editable?) {
                if (query != null){
                    val filtered = ArrayList<Jamaah.DataItem>()
                    for (i in list ){
                        if (i.nama?.lowercase(Locale.ROOT)?.contains(query) == true){
                            filtered.add(i)
                        }else if(i.noTelepon?.contains(query)==true){
                            filtered.add(i)
                        }
                    }
                    if (filtered.isNotEmpty()){
                        binding.notFound.visibility = View.GONE
                        adapter.filteredList(filtered)
                        binding.daftar.visibility = View.VISIBLE
                    }else{
                        binding.notFound.visibility = View.VISIBLE
                        binding.daftar.visibility = View.INVISIBLE
                    }
                }
            }

        })

        daftar = binding.daftar
        daftar.layoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL,false)

        getDataJamaah(intent.getStringExtra("key"),intent.getStringExtra("id"))

    }

    override fun onResume() {
        super.onResume()
        getDataJamaah(intent.getStringExtra("key"),intent.getStringExtra("id"))
    }
}