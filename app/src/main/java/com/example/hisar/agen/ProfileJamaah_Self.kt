package com.example.hisar.agen

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hisar.R
import com.example.hisar.api.ApiClient
import com.example.hisar.data.DocJamaah
import com.example.hisar.data.Message
import com.example.hisar.data.PerkabJamaah
import com.example.hisar.data.ReqJamaahDelete
import com.example.hisar.data.RequestId
import com.example.hisar.databinding.ActivityProfileJamaahSelfBinding
import com.example.hisar.databinding.ConfirmPopupBinding
import com.example.hisar.databinding.ImagePopupBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("SetTextI18n")
class ProfileJamaah_Self : AppCompatActivity() {

    private lateinit var binding: ActivityProfileJamaahSelfBinding
    private lateinit var riwayat:RecyclerView
    private var img: ArrayList<String> = arrayListOf("1","2","3","4","5")

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
                        img[0] = "https://api.hisar.my.id/${res.data.ktp}"
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
                            .load("https://api.hisar.my.id/${res.data.akteK}")
                            .placeholder(R.drawable.image_placeholder)
                            .into(binding.akteLahir)
                        Glide
                            .with(applicationContext)
                            .load("https://api.hisar.my.id/${res.data.akteN}")
                            .placeholder(R.drawable.image_placeholder)
                            .into(binding.akteNikah)
                        Glide
                            .with(applicationContext)
                            .load("https://api.hisar.my.id/${res.data.foto}")
                            .placeholder(R.drawable.image_placeholder)
                            .into(binding.pasFoto)
                        Glide
                            .with(applicationContext)
                            .load("https://api.hisar.my.id/${res.data.passport}")
                            .placeholder(R.drawable.image_placeholder)
                            .into(binding.fotoPaspor)

                    }
                }

                override fun onFailure(call: Call<DocJamaah>, t: Throwable) {
                    Toast.makeText(applicationContext,"${t.message}",Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun deleteJamaah(key:String,id: String,userId:String){
        ApiClient.apiService.hapusJamaah(key, ReqJamaahDelete(id,userId))
            .enqueue(object : Callback<Message>{
                override fun onResponse(call: Call<Message>, response: Response<Message>) {
                    if (response.isSuccessful){
                        startActivity(Intent(applicationContext,AgenActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        )
                        finish()
                    }
                }

                override fun onFailure(call: Call<Message>, t: Throwable) {
                    Toast.makeText(applicationContext,"Server Error!",Toast.LENGTH_SHORT).show()
                }

            })
    }

    override fun onResume() {
        super.onResume()

        val sharedPreferences = getSharedPreferences("AUTH", Context.MODE_PRIVATE)
        val to:String = sharedPreferences.getString("KEY",null).toString()

        getDoc(to,intent.getStringExtra("id").toString())
        getPerkab(to,intent.getStringExtra("id").toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileJamaahSelfBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("AUTH", Context.MODE_PRIVATE)
        val to:String = sharedPreferences.getString("KEY",null).toString()
        sharedPreferences.getString("ROLE",null).toString()
        riwayat = binding.listRiwayat

        riwayat.layoutManager = object : LinearLayoutManager(applicationContext){
            override fun canScrollVertically(): Boolean {
                return false
            }

            override fun canScrollHorizontally(): Boolean {
                return false
            }
        }


        intent.getStringExtra("id")?.let { getPerkab(to, it) }
        intent.getStringExtra("id")?.let { getDoc(to, it)}

        binding.editDokumen.setOnClickListener {
            val intent = Intent(applicationContext,EditDokumenJamaah::class.java)
                .putExtra("id",intent.getStringExtra("id"))
            startActivity(intent)
        }

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

        binding.edit.setOnClickListener {
            val intent = Intent(applicationContext,EditJamaah::class.java)
                .putExtra("id",intent.getStringExtra("id"))
                .putExtra("ktp",intent.getStringExtra("ktp"))
                .putExtra("nama",intent.getStringExtra("nama"))
                .putExtra("telp",intent.getStringExtra("telp"))
                .putExtra("kelamin",if (intent.getStringExtra("kelamin") == "Laki-Laki") 0 else 1)
                .putExtra("alamat",intent.getStringExtra("alamat"))
                .putExtra("paket",intent.getStringExtra("paket"))
            startActivity(intent)
        }

        binding.editPerkab.setOnClickListener {
            val intent = Intent(applicationContext,EditPerlengkapanJamaah::class.java)
                .putExtra("koper",binding.koper.isChecked)
                .putExtra("batik",binding.batik.isChecked)
                .putExtra("ihram",binding.ihram.isChecked)
                .putExtra("mukena",binding.mukena.isChecked)
                .putExtra("syal",binding.syal.isChecked)
                .putExtra("panduan",binding.panduan.isChecked)
                .putExtra("doa",binding.doa.isChecked)
                .putExtra("sholawat",binding.sholawat.isChecked)
                .putExtra("peta",binding.peta.isChecked)
                .putExtra("id",intent.getStringExtra("id"))
            startActivity(intent)
        }


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

        binding.delete.setOnClickListener {
            val dialog = Dialog(this)
            val bind = ConfirmPopupBinding.inflate(layoutInflater)
            dialog.setContentView(bind.root)
            bind.text.text = "Yakin Menghapus Jamaah " + intent.getStringExtra("nama").toString()
            bind.yes.setOnClickListener {
                deleteJamaah(to,intent.getStringExtra("id").toString(),intent.getStringExtra("userId").toString())
            }
            bind.no.setOnClickListener {
                dialog.dismiss()
            }
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

}