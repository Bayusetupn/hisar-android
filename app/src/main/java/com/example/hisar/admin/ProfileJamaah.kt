package com.example.hisar.admin

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.scale
import androidx.core.view.drawToBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hisar.DaftarRiwayatAdapter
import com.example.hisar.R
import com.example.hisar.api.ApiClient
import com.example.hisar.data.DocJamaah
import com.example.hisar.data.Message
import com.example.hisar.data.PerkabJamaah
import com.example.hisar.data.RequestId
import com.example.hisar.data.ResRiwayatJamaah
import com.example.hisar.databinding.ActivityProfileJamaahBinding
import com.example.hisar.databinding.ConfirmPopupBinding
import com.example.hisar.databinding.ImagePopupBinding
import com.example.hisar.databinding.WarningPopupBinding
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileJamaah : AppCompatActivity() {

    private lateinit var binding: ActivityProfileJamaahBinding
    private lateinit var riwayat:RecyclerView

    private fun showWarning(key: String,id: String,text:String,to:String){
        val dialog = Dialog(this)
        val bind = ConfirmPopupBinding.inflate(layoutInflater)
        bind.icon.setImageResource(R.drawable.warning)
        bind.no.setOnClickListener {
            dialog.dismiss()
        }
        bind.text.text = text
        bind.yes.text = "Yakin"
        bind.yes.setOnClickListener {
            when(to){
                "dp"->{
                    dialog.dismiss()
                    setDp(key,id)
                }
            }
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(bind.root)
        dialog.show()
    }

    private fun getRiwayat(key: String,id: String){
        ApiClient.apiService.jamaahJadwal(key,RequestId(id))
            .enqueue(object: Callback<ResRiwayatJamaah>{
                override fun onResponse(
                    call: Call<ResRiwayatJamaah>,
                    response: Response<ResRiwayatJamaah>
                ) {
                    if (response.isSuccessful){
                        val res = response.body()!!
                        riwayat.adapter = DaftarRiwayatAdapter(res.data)
                    }
                }

                override fun onFailure(call: Call<ResRiwayatJamaah>, t: Throwable) {
                    Toast.makeText(applicationContext,"Server Error!",Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun setDp(key:String,id:String){
        ApiClient.apiService.jamaahDp(key,RequestId(id))
            .enqueue(object: Callback<Message>{
                override fun onResponse(call: Call<Message>, response: Response<Message>) {
                    if (response.isSuccessful){
                        Toast.makeText(applicationContext,"Sukses Merubah Status Dp!",Toast.LENGTH_SHORT).show()
                        binding.keterangan.text = "Lunas"
                        binding.dp.visibility = View.GONE
                    }else{
                        val res = Gson().fromJson(response.errorBody()?.string(),Message::class.java)
                        Toast.makeText(applicationContext,res.message.toString(),Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Message>, t: Throwable) {
                    Toast.makeText(applicationContext,"${t.message}",Toast.LENGTH_SHORT).show()
                }

            })
    }
    private fun getPerkab(key: String?,id: String){
        ApiClient.apiService.perkab(key, RequestId(id))
            .enqueue(object: Callback<PerkabJamaah>{
                override fun onResponse(
                    call: Call<PerkabJamaah>,
                    response: Response<PerkabJamaah>
                ) {
                    if (response.isSuccessful) {
                        val res = response.body()!!
                        val cek = res.data
                        binding.koper.isChecked = cek.koperDll
                        binding.batik.isChecked = cek.kainBatik
                        binding.ihram.isChecked = cek.kainIhram
                        binding.mukena.isChecked = cek.mukena
                        binding.syal.isChecked = cek.syal
                        binding.panduan.isChecked = cek.bukuPanduan
                        binding.doa.isChecked = cek.bukuDoa
                        binding.sholawat.isChecked = cek.bookletSholawat
                        binding.peta.isChecked = cek.bookletPeta

                    }
                }

                override fun onFailure(call: Call<PerkabJamaah>, t: Throwable) {
                    Toast.makeText(applicationContext,"${t.message}",Toast.LENGTH_SHORT).show()
                }

            })
    }


    private fun getDoc(key: String?,id:String){
        ApiClient.apiService.jamaahDoc(key,RequestId(id))
            .enqueue(object: Callback<DocJamaah>{
                override fun onResponse(call: Call<DocJamaah>, response: Response<DocJamaah>) {
                    if (response.isSuccessful){
                        val res = response.body()!!
                        Glide
                            .with(applicationContext)
                            .load("https://api.hisar.my.id/${res.data.ktp}")
                            .placeholder(R.drawable.image_placeholder)
                            .into(binding.fotoKtp)
                        Glide
                            .with(applicationContext)
                            .load("https://api.hisar.my.id/${res.data.kk}")
                            .placeholder(R.drawable.image_placeholder)
                            .into(binding.fotoKk)
                        Glide
                            .with(applicationContext)
                            .load("https://api.hisar.my.id${res.data.akteK}")
                            .placeholder(R.drawable.image_placeholder)
                            .into(binding.akteLahir)
                        Glide
                            .with(applicationContext)
                            .load("https://api.hisar.my.id${res.data.akteN}")
                            .placeholder(R.drawable.image_placeholder)
                            .into(binding.akteNikah)
                        Glide
                            .with(applicationContext)
                            .load("https://api.hisar.my.id${res.data.foto}")
                            .placeholder(R.drawable.image_placeholder)
                            .into(binding.pasFoto)
                        Glide
                            .with(applicationContext)
                            .load("https://api.hisar.my.id${res.data.passport}")
                            .placeholder(R.drawable.image_placeholder)
                            .into(binding.fotoPaspor)
                    }
                }

                override fun onFailure(call: Call<DocJamaah>, t: Throwable) {
                    Toast.makeText(applicationContext,"${t.message}",Toast.LENGTH_SHORT).show()
                }

            })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileJamaahBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("AUTH", Context.MODE_PRIVATE)
        val to:String = sharedPreferences.getString("KEY",null).toString()
        val role:String = sharedPreferences.getString("ROLE",null).toString()
        riwayat = binding.listRiwayat

        riwayat.layoutManager = object : LinearLayoutManager(applicationContext){
            override fun canScrollVertically(): Boolean {
                return false
            }

            override fun canScrollHorizontally(): Boolean {
                return false
            }
        }

        getRiwayat(to,intent.getStringExtra("id").toString())

        role.apply {
            when(this){
                "admin"-> binding.editStatusJamaah.visibility = View.GONE
                "manager"->binding.editStatusJamaah.visibility = View.VISIBLE
            }
        }

        intent.getBooleanExtra("keterangan",false).apply {
            when(this){
                true->binding.dp.visibility = View.GONE
                false->binding.dp.visibility = View.VISIBLE
            }
        }

        binding.jadwal.setOnClickListener {
            binding.riwayat.visibility.apply {
                when(this){
                    View.VISIBLE-> binding.riwayat.visibility = View.GONE
                    View.GONE -> binding.riwayat.visibility = View.VISIBLE
                }
            }
        }

        binding.dp.setOnClickListener{
            showWarning(to,intent.getStringExtra("id").toString(),"Yakin Merubah Status Dp ?","dp")
        }

        intent.getStringExtra("id")?.let { getPerkab(to, it) }
        intent.getStringExtra("id")?.let { getDoc(to, it) }

        //profile
        binding.nama.text = intent.getStringExtra("nama")
        binding.alamat.text = intent.getStringExtra("alamat")
        binding.ktp.text = intent.getStringExtra("ktp")
        binding.telp.text = intent.getStringExtra("telp")
        binding.kelamin.text = intent.getStringExtra("kelamin")
        binding.gabung.text = intent.getStringExtra("bergabung")
        binding.berangkat.text = if (intent.getStringExtra("berangkat") == null) "Belum ditentukan" else intent.getStringExtra("berangkat")
        binding.paket.text = intent.getStringExtra("paket")
        binding.daftarkan.text = intent.getStringExtra("didaftarkan")
        binding.keterangan.text = if (intent.getBooleanExtra("keterangan",false)) "Lunas" else "Belum Lunas"

        binding.fotoKtp.setOnClickListener {
            val dialog = Dialog(this)
            val bind = ImagePopupBinding.inflate(layoutInflater)
            dialog.setContentView(bind.root)
            bind.img.setImageDrawable(binding.fotoKtp.drawable)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }

        binding.fotoKk.setOnClickListener {
            val dialog = Dialog(this)
            val bind = ImagePopupBinding.inflate(layoutInflater)
            dialog.setContentView(bind.root)
            bind.img.setImageDrawable(binding.fotoKk.drawable)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }

        binding.akteLahir.setOnClickListener {
            val dialog = Dialog(this)
            val bind = ImagePopupBinding.inflate(layoutInflater)
            dialog.setContentView(bind.root)
            bind.img.setImageDrawable(binding.akteLahir.drawable)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }
        binding.akteNikah.setOnClickListener {
            val dialog = Dialog(this)
            val bind = ImagePopupBinding.inflate(layoutInflater)
            dialog.setContentView(bind.root)
            bind.img.setImageDrawable(binding.akteNikah.drawable)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }
        binding.pasFoto.setOnClickListener {
            val dialog = Dialog(this)
            val bind = ImagePopupBinding.inflate(layoutInflater)
            dialog.setContentView(bind.root)
            bind.img.setImageDrawable(binding.pasFoto.drawable)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }
        binding.fotoPaspor.setOnClickListener {
            val dialog = Dialog(this)
            val bind = ImagePopupBinding.inflate(layoutInflater)
            dialog.setContentView(bind.root)
            bind.img.setImageDrawable(binding.fotoPaspor.drawable)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }

        //back
        binding.textView2.setOnClickListener {
            onBackPressed()
        }
        binding.imageView2.setOnClickListener {
            onBackPressed()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}