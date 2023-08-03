package com.example.hisar.agen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hisar.DaftarAgenAdapter_Agen
import com.example.hisar.admin.Agen_Rank
import com.example.hisar.api.ApiClient
import com.example.hisar.data.Data
import com.example.hisar.databinding.FragmentAgenDaftarBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class AgenDaftar : Fragment() {

    private var _binding:FragmentAgenDaftarBinding?=null
    private val binding get() = _binding!!
    private lateinit var daftar:RecyclerView
    private lateinit var adapter:DaftarAgenAdapter_Agen
    private lateinit var list:ArrayList<Data.DataAgen>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAgenDaftarBinding.inflate(inflater,container,false)
        return binding.root
    }

    companion object {
        fun setData(data:String?,role:String?): AgenDaftar {
            val fragment = AgenDaftar()
            val key = Bundle()
            key.putString("auth_key",data)
            key.putString("role",role)
            fragment.arguments = key
            return fragment
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        daftar = binding.daftar
        binding.textView.text = "Daftar ${arguments?.getString("role")}"
        daftar.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

        when(arguments?.getString("role").toString()){
            "agen"->{
                arguments?.getString("auth_key")?.let { getAgen(it) }
            }
            "ustad"->{
                arguments?.getString("auth_key")?.let { getUstad(it) }
            }
        }

        binding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(query: Editable?) {
                if (query != null){
                    val filtered = ArrayList<Data.DataAgen>()
                    for (i in list ){
                        if (i.nama?.lowercase(Locale.ROOT)?.contains(query) == true){
                            filtered.add(i)
                        }else if(i.noTelepon?.contains(query)==true){
                            filtered.add(i)
                        }
                    }
                    if (filtered.isNotEmpty()){
                        binding.notFound.text = "${arguments?.getString("role").toString().replaceFirstChar { it.uppercase() } }} Tidak Ditemukan"
                        binding.notFound.visibility = View.GONE
                        adapter.filteredList(filtered)
                        binding.daftar.visibility = View.VISIBLE
                    }else{
                        binding.notFound.text = "${arguments?.getString("role")} Tidak Ditemukan"
                        binding.notFound.visibility = View.VISIBLE
                        binding.daftar.visibility = View.INVISIBLE
                    }
                }
            }

        })

        binding.rank.setOnClickListener {
            val intent = Intent(context,Agen_Rank::class.java)
                .putExtra("role",arguments?.getString("role"))
            startActivity(intent)
        }


    }

    private fun getAgen(key:String){
        ApiClient.apiService.agen(key)
            .enqueue(object: Callback<Data>{
                override fun onResponse(call: Call<Data>, response: Response<Data>) {
                    if (response.isSuccessful){
                        val res = response.body()!!
                        if (res.data.size < 1){
                            binding.load.visibility = View.GONE
                            binding.daftar.visibility = View.GONE
                            binding.notFound.visibility = View.VISIBLE
                        }else{
                        list = res.data
                        adapter = DaftarAgenAdapter_Agen(res.data,res.data.size)
                        daftar.adapter = adapter
                            binding.load.visibility = View.GONE
                        binding.notFound.visibility = View.GONE
                        binding.daftar.visibility = View.VISIBLE
                        }
                    }else {
                        binding.load.visibility = View.GONE
                        binding.daftar.visibility = View.GONE
                        binding.notFound.visibility = View.VISIBLE
                    }

                }

                override fun onFailure(call: Call<Data>, t: Throwable) {
                    Toast.makeText(context,"Server Error!",Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun getUstad(key:String){
        ApiClient.apiService.ustad(key)
            .enqueue(object: Callback<Data>{
                override fun onResponse(call: Call<Data>, response: Response<Data>) {
                    if (response.isSuccessful){
                        val res = response.body()!!
                        if (res.data.size < 1){
                            binding.load.visibility = View.GONE
                            binding.daftar.visibility = View.GONE
                            binding.notFound.visibility = View.VISIBLE
                        }else{
                            list = res.data
                            adapter = DaftarAgenAdapter_Agen(res.data,res.data.size)
                            daftar.adapter = adapter
                            binding.load.visibility = View.GONE
                            binding.notFound.visibility = View.GONE
                            binding.daftar.visibility = View.VISIBLE
                        }
                    }else {
                        binding.load.visibility = View.GONE
                        binding.daftar.visibility = View.GONE
                        binding.notFound.visibility = View.VISIBLE
                    }

                }

                override fun onFailure(call: Call<Data>, t: Throwable) {
                    Toast.makeText(context,"Server Error!",Toast.LENGTH_SHORT).show()
                }

            })
    }

}