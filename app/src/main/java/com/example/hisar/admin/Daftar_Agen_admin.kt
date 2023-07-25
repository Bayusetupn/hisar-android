package com.example.hisar.admin

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hisar.DaftarAgenAdapter
import com.example.hisar.api.ApiClient
import com.example.hisar.data.Data
import com.example.hisar.databinding.FragmentDaftarAgenAdminBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class DaftarAgenAdmin : Fragment() {

    private var _binding: FragmentDaftarAgenAdminBinding? = null
    private val binding get() = _binding!!
    private lateinit var daftar:RecyclerView
    private lateinit var adapter: DaftarAgenAdapter
    private lateinit var list:ArrayList<Data.DataAgen>

    companion object {
        fun setData(data:String?): DaftarAgenAdmin{
            val fragment = DaftarAgenAdmin()
            val key = Bundle()
            key.putString("auth_key",data)
            fragment.arguments = key
            return fragment
        }
    }
    private fun getData(){
        arguments?.getString("auth_key")?.let {
            ApiClient.apiService.agen(it)
                .enqueue(object: Callback<Data>{
                    override fun onResponse(call: Call<Data>, response: Response<Data>) {
                        if (response.isSuccessful){
                            val res = response.body()!!
                            binding.load.visibility = View.GONE
                            binding.daftar.visibility = View.VISIBLE
                            adapter = DaftarAgenAdapter(res.data,res.data.size)
                            list = res.data
                            daftar.adapter = adapter
                        }
                    }

                    override fun onFailure(call: Call<Data>, t: Throwable) {
                        Toast.makeText(context,"Server Error!",Toast.LENGTH_SHORT).show()
                    }

                })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDaftarAgenAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.search.addTextChangedListener(object : TextWatcher{
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

        daftar.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

        binding.add.setOnClickListener{
            startActivity(Intent(context,TambahAgenAcitvity::class.java))
        }
        getData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}