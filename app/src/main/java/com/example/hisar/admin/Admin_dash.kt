package com.example.hisar.admin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hisar.DaftarAgenAdapter
import com.example.hisar.R
import com.example.hisar.api.ApiClient
import com.example.hisar.data.Data
import com.example.hisar.data.Jamaah
import com.example.hisar.databinding.FragmentAdminDashBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Admin_dash : Fragment() {

    private var _binding: FragmentAdminDashBinding? = null
    private val binding get() = _binding!!
    private lateinit var agen:RecyclerView
    private lateinit var ustad:RecyclerView

    private fun DataAgen(){
        arguments?.getString("auth_key")?.let {
            ApiClient.apiService.agen(it)
                .enqueue(object: Callback<Data>{
                    override fun onResponse(call: Call<Data>, response: Response<Data>) {
                        if (response.isSuccessful){
                            val res = response.body()!!
                            agen.adapter = DaftarAgenAdapter(res.data,2)
                            binding.totalAgen.text = res.data.size.toString()
                            binding.load.visibility = View.GONE
                            binding.daftar.visibility = View.VISIBLE
                        }
                    }

                    override fun onFailure(call: Call<Data>, t: Throwable) {
                        Toast.makeText(context,"Server Error!", Toast.LENGTH_SHORT).show()
                    }

                })
        }
    }

    private fun DataUstad(){
        arguments?.getString("auth_key")?.let {
            ApiClient.apiService.ustad(it)
                .enqueue(object: Callback<Data>{
                    override fun onResponse(call: Call<Data>, response: Response<Data>) {
                        if (response.isSuccessful){
                            val res= response.body()!!
                            ustad.adapter = DaftarAgenAdapter(res.data,2)
                            binding.totalUstad.text = res.data.size.toString()
                            binding.load.visibility = View.GONE
                            binding.daftar.visibility = View.VISIBLE
                        }
                    }

                    override fun onFailure(call: Call<Data>, t: Throwable) {
                        Toast.makeText(context,"Server Error!",Toast.LENGTH_SHORT).show()
                    }

                })
        }

    }

    private fun DataJamaah(){
        arguments?.getString("auth_key")?.let {
            ApiClient.apiService.jamaah(it)
                .enqueue(object: Callback<Jamaah>{
                    override fun onResponse(call: Call<Jamaah>, response: Response<Jamaah>) {
                        if (response.isSuccessful){
                            val res= response.body()!!
                            binding.totalJamaah.text = res.data.size.toString()
                            binding.load.visibility = View.GONE
                            binding.daftar.visibility = View.VISIBLE

                        }
                    }

                    override fun onFailure(call: Call<Jamaah>, t: Throwable) {
                        Toast.makeText(context,"Server Error!",Toast.LENGTH_SHORT).show()
                    }

                })
        }

    }

    companion object {
        fun setData(data:String?): Admin_dash{
            val fragment = Admin_dash()
            val key = Bundle()
            key.putString("auth_key",data)
            fragment.arguments = key
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAdminDashBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        agen = binding.daftarAgen
        ustad = binding.daftarUstad
        agen.layoutManager = object : LinearLayoutManager(context){
            override fun canScrollVertically(): Boolean {
                return false
            }

            override fun canScrollHorizontally(): Boolean {
                return false
            }
        }
        ustad.layoutManager = object : LinearLayoutManager(context){
            override fun canScrollVertically(): Boolean {
                return false
            }

            override fun canScrollHorizontally(): Boolean {
                return false
            }
        }

        binding.moreAgen.setOnClickListener {
            requireActivity().findViewById<BottomNavigationView>(R.id.admin_nav).selectedItemId = R.id.agen
        }

        binding.moteUstad.setOnClickListener {
            requireActivity().findViewById<BottomNavigationView>(R.id.admin_nav).selectedItemId = R.id.ustad
        }

        binding.profile.setOnClickListener{
            val intent = Intent(context,ProfileAdminActivity::class.java)
            intent.putExtra("key",arguments?.getString("auth_key"))
            intent.putExtra("totalUstad",binding.totalUstad.text)
            intent.putExtra("totalAgen",binding.totalAgen.text)
            intent.putExtra("totalJamaah",binding.totalJamaah.text)
            startActivity(intent)
        }
        DataAgen()
        DataUstad()
        DataJamaah()

    }


}