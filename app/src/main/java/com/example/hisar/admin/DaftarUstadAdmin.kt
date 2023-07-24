package com.example.hisar.admin

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hisar.DaftarAgenAdapter
import com.example.hisar.api.ApiClient
import com.example.hisar.data.Data
import com.example.hisar.databinding.FragmentDaftarUstadAdminBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DaftarUstadAdmin : Fragment() {

    private var _binding: FragmentDaftarUstadAdminBinding? = null
    private val binding get() = _binding!!
    private lateinit var daftar:RecyclerView

    companion object {
        fun setData(data:String?): DaftarUstadAdmin{
            val fragment = DaftarUstadAdmin()
            val key = Bundle()
            key.putString("auth_key",data)
            fragment.arguments = key
            return fragment
        }
    }

    private fun getData(){
        arguments?.getString("auth_key")?.let {
            ApiClient.apiService.ustad(it)
                .enqueue(object: Callback<Data>{
                    override fun onResponse(call: Call<Data>, response: Response<Data>) {
                        if (response.isSuccessful){
                            val res = response.body()!!
                            daftar.adapter = DaftarAgenAdapter(res.data,res.data.size)
                            binding.load.visibility = View.GONE
                            binding.daftar.visibility = View.VISIBLE
                        }
                    }

                    override fun onFailure(call: Call<Data>, t: Throwable) {
                        TODO("Not yet implemented")
                    }

                })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDaftarUstadAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        daftar = binding.daftar

        daftar.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

        getData()

        binding.add.setOnClickListener{
            startActivity(Intent(context,TambahUstad::class.java))
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}