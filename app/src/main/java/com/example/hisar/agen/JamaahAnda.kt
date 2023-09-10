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
import com.example.hisar.api.ApiClient
import com.example.hisar.data.Jamaah
import com.example.hisar.data.RequestId
import com.example.hisar.databinding.FragmentJamaahAndaBinding
import com.example.hisar.login.DaftarJamaahAdapter_Self
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

@SuppressLint("SetTextI18n")
class JamaahAnda : Fragment() {
    private var _binding:FragmentJamaahAndaBinding?=null
    private val binding get() = _binding!!

    private lateinit var daftar: RecyclerView
    private lateinit var adapter: DaftarJamaahAdapter_Self
    private lateinit var list:ArrayList<Jamaah.DataItem>

    companion object {
        fun setData(data:String?,role:String?,id:String?): JamaahAnda {
            val fragment = JamaahAnda()
            val key = Bundle()
            key.putString("auth_key",data)
            key.putString("role",role)
            key.putString("id",id)
            fragment.arguments = key
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJamaahAndaBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        daftar = binding.daftar
        daftar.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        getJamaah(arguments?.getString("auth_key").toString(),arguments?.getString("id").toString())

        binding.add.setOnClickListener {
            startActivity(Intent(context,TambahJamaahActivity::class.java)
                .putExtra("id",arguments?.getString("id"))
            )
        }

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
                        binding.notFound.text = "Jamaah Tidak Ditemukan"
                        binding.notFound.visibility = View.VISIBLE
                        binding.daftar.visibility = View.INVISIBLE
                    }
                }
            }

        })

    }

    override fun onResume() {
        super.onResume()
        getJamaah(arguments?.getString("auth_key").toString(),arguments?.getString("id").toString())
    }

    private fun getJamaah(key:String,id: String){
        ApiClient.apiService.jamaah(key, RequestId(id))
            .enqueue(object: Callback<Jamaah>{
                override fun onResponse(call: Call<Jamaah>, response: Response<Jamaah>) {
                    if (response.isSuccessful){
                        val res = response.body()!!
                        if (res.data.size < 1){
                            binding.notFound.text = "Jamaah Belum ada"
                            binding.notFound.visibility = View.VISIBLE
                            binding.load.visibility = View.GONE
                            binding.daftar.visibility = View.VISIBLE
                        }else{
                            binding.load.visibility = View.GONE
                            binding.notFound.visibility = View.GONE
                            binding.daftar.visibility = View.VISIBLE
                            list = res.data
                            adapter = DaftarJamaahAdapter_Self(res.data,res.data.size)
                            daftar.adapter = adapter
                        }

                    }
                }

                override fun onFailure(call: Call<Jamaah>, t: Throwable) {
                    Toast.makeText(context,"Server Error!",Toast.LENGTH_SHORT).show()
                }

            })
    }

}