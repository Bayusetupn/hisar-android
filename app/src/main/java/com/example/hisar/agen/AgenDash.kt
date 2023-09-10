package com.example.hisar.agen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.bumptech.glide.Glide
import com.example.hisar.DaftarAgenAdapter_Agen
import com.example.hisar.R
import com.example.hisar.SlideAdapter
import com.example.hisar.api.ApiClient
import com.example.hisar.data.Banner
import com.example.hisar.data.Data
import com.example.hisar.data.Jamaah
import com.example.hisar.data.RequestId
import com.example.hisar.databinding.FragmentAgenDashBinding
import com.example.hisar.login.DaftarJamaahAdapter_Self
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AgenDash : Fragment() {

    private var _binding: FragmentAgenDashBinding? = null
    private val binding get() = _binding!!
    private lateinit var daftar:RecyclerView
    private lateinit var jamaahAnda:RecyclerView
    private lateinit var adapter : SlideAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var handler: Handler
    private var slide: Boolean = false
    private var sizes: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAgenDashBinding.inflate(inflater,container,false)
        return binding.root

    }

    companion object {
        fun setData(data:String?,role:String?,id:String?): AgenDash {
            val fragment = AgenDash()
            val key = Bundle()
            key.putString("auth_key",data)
            key.putString("role",role)
            key.putString("id",id)
            fragment.arguments = key
            return fragment
        }
    }

    private fun agen(key:String){
        ApiClient.apiService.agen(key)
            .enqueue(object: Callback<Data>{
                override fun onResponse(call: Call<Data>, response: Response<Data>) {
                    if (response.isSuccessful){
                        val res= response.body()!!
                        if (res.data.size < 1){
                            binding.totalAgen.text = "0"
                            binding.notFoundAgen.visibility = View.VISIBLE
                            binding.daftarAgen.visibility = View.GONE
                        }else{
                            binding.totalAgen.text = res.data.size.toString()
                            binding.notFoundAgen.visibility = View.GONE
                            binding.daftarAgen.visibility = View.VISIBLE
                            daftar.adapter = DaftarAgenAdapter_Agen(res.data,2)
                        }
                    }
                }

                override fun onFailure(call: Call<Data>, t: Throwable) {
                    Toast.makeText(context,"Server Error!",Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun ustad(key:String){
        ApiClient.apiService.ustad(key)
            .enqueue(object: Callback<Data>{
                override fun onResponse(call: Call<Data>, response: Response<Data>) {
                    if (response.isSuccessful){
                        val res= response.body()!!
                        if (res.data.size < 1){
                            binding.totalAgen.text = "0"
                            binding.notFoundAgen.visibility = View.VISIBLE
                            binding.daftarAgen.visibility = View.GONE
                        }else{
                            binding.totalAgen.text = res.data.size.toString()
                            binding.notFoundAgen.visibility = View.GONE
                            binding.daftarAgen.visibility = View.VISIBLE
                            daftar.adapter = DaftarAgenAdapter_Agen(res.data,2)
                        }
                    }
                }

                override fun onFailure(call: Call<Data>, t: Throwable) {
                    Toast.makeText(context,"Server Error!",Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun JamaahBulanIni(data: ArrayList<Jamaah.DataItem>): Int{
        val month = LocalDate.now().month
        DateTimeFormatter.ofPattern("yyyy-MM-dd")
       return data.map {
           LocalDate.parse(it.dibuatPada)
        }.filter {
            it.month == month
        }.size
    }
    private fun profile(key: String){
        ApiClient.apiService.profile(key)
            .enqueue(object: Callback<Data>{
                override fun onResponse(call: Call<Data>, response: Response<Data>) {
                    if (response.isSuccessful){
                        val res = response.body()!!
                        val data = res.data[0]
                        Glide
                            .with(requireActivity().applicationContext)
                            .load("https://api.hisar.my.id/" + res.data[0].foto)
                            .placeholder(R.drawable.user)
                            .into(binding.profilePic)
                        binding.profilePic.setOnClickListener {
                            val intent = Intent(context,AgenProfile::class.java)
                                .putExtra("nama",data.nama)
                                .putExtra("ktp",data.noKtp)
                                .putExtra("alamat",data.alamat)
                                .putExtra("total",data.totalJamaah)
                                .putExtra("bergabung",data.dibuatPada)
                                .putExtra("username",data.username)
                                .putExtra("id",data.id)
                                .putExtra("telp",data.noTelepon)
                                .putExtra("role",data.role)
                                .putExtra("foto",data.foto)
                            startActivity(intent)
                        }
                    }
                }

                override fun onFailure(call: Call<Data>, t: Throwable) {
                    Toast.makeText(context,"Server Error!",Toast.LENGTH_SHORT).show()
                }

            })
    }
    private fun getJamaah(key: String,id:String){
        ApiClient.apiService.jamaah(key, RequestId(id))
            .enqueue(object: Callback<Jamaah>{
                @SuppressLint("SetTextI18n")
                override fun onResponse(call: Call<Jamaah>, response: Response<Jamaah>) {
                    if (response.isSuccessful){
                        val res = response.body()!!
                        if (res.data.size < 1){
                            binding.jamaahAnda.text = "0"
                            binding.totalJamaah.text = "0"
                            binding.notFoundJamaah.visibility = View.VISIBLE
                            binding.daftarJamaah.visibility = View.GONE
                        }else{
                            binding.totalJamaah.text = res.data.size.toString()
                            binding.jamaahAnda.text = "+ ${JamaahBulanIni(res.data)}"
                            binding.notFoundJamaah.visibility = View.GONE
                            jamaahAnda.adapter = DaftarJamaahAdapter_Self(res.data,2)
                            binding.daftarJamaah.visibility = View.VISIBLE
                        }
                    }
                }

                override fun onFailure(call: Call<Jamaah>, t: Throwable) {
                    Toast.makeText(context,"Server Error!",Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun promo(key:String){
        ApiClient.apiService.getPromo(key)
            .enqueue(object: Callback<Banner>{
                override fun onResponse(call: Call<Banner>, response: Response<Banner>) {
                    if (response.isSuccessful){
                        val res = response.body()!!
                        adapter = SlideAdapter(res.data,viewPager)
                        viewPager.adapter = adapter
                        slide = res.data.size > 1
                        sizes = res.data.size
                        binding.promoPlaceholder.visibility = View.GONE
                        binding.promo.visibility = View.VISIBLE
                        binding.indicator.visibility = View.VISIBLE
                        binding.indicator.createIndicators(res.data.size,0)
                    }
                }

                override fun onFailure(call: Call<Banner>, t: Throwable) {
                    Toast.makeText(context,"Server Error!",Toast.LENGTH_SHORT).show()
                }

            })
    }

    override fun onResume() {
        super.onResume()
        if (slide){
            handler.postDelayed(runnable,3000)
        }
        profile(arguments?.getString("auth_key").toString())
        getJamaah(arguments?.getString("auth_key").toString(),arguments?.getString("id").toString())
        when(arguments?.getString("role")){
            "agen"->{
                agen(arguments?.getString("auth_key").toString())
            }
            "ustad"->{
                ustad(arguments?.getString("auth_key").toString())
            }
        }
    }

    private fun setupTransformer(){
        val transformer  = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer{page,position->
            run {
                val r = 1 - kotlin.math.abs(position)
                page.scaleY = 0.85f + r * 0.14f
            }
        }

        viewPager.setPageTransformer(transformer)
    }

    private val runnable = Runnable{
        viewPager.currentItem = viewPager.currentItem + 1
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager = binding.promo
        handler = Handler(Looper.myLooper()!!)
        viewPager.clipToPadding = false
        viewPager.clipChildren = false
        viewPager.offscreenPageLimit = 3
        viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        setupTransformer()


        viewPager.registerOnPageChangeCallback(object: OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (slide){
                    binding.indicator.animatePageSelected(position % sizes)
                    handler.removeCallbacks(runnable)
                    handler.postDelayed(runnable,3000)
                }

            }
        } )

        promo(arguments?.getString("auth_key").toString())

        binding.morePromo.setOnClickListener {
            startActivity(Intent(context,PromoActivity::class.java))
        }

        profile(arguments?.getString("auth_key").toString())
        val roles = arguments?.getString("role").toString().replaceFirstChar { it.uppercase() }
        binding.daftarRole.text = "Daftar $roles"
        binding.total.text = "Total $roles"

        daftar = binding.daftarAgen
        jamaahAnda = binding.daftarJamaah

        jamaahAnda.layoutManager = object: LinearLayoutManager(context){
            override fun canScrollHorizontally(): Boolean {
                return false
            }

            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        daftar.layoutManager = object : LinearLayoutManager(context){
            override fun canScrollHorizontally(): Boolean {
                return false
            }

            override fun canScrollVertically(): Boolean {
                return false
            }
        }

        binding.moreAgen.setOnClickListener {
            requireActivity().findViewById<BottomNavigationView>(R.id.admin_nav).selectedItemId = R.id.agen_agen
        }

        binding.moreJamaah.setOnClickListener {
            requireActivity().findViewById<BottomNavigationView>(R.id.admin_nav).selectedItemId = R.id.agen_jamaah
        }

        binding.notFoundAgenText.text = "${arguments?.getString("role").toString().replaceFirstChar { it.uppercase() }}Tidak Ada"
        when(arguments?.getString("role")){
            "agen"->{
                agen(arguments?.getString("auth_key").toString())
            }
            "ustad"->{
                ustad(arguments?.getString("auth_key").toString())
            }
        }
        getJamaah(arguments?.getString("auth_key").toString(),arguments?.getString("id").toString())

    }

}