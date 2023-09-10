package com.example.hisar.admin

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hisar.DaftarRiwayatAdapter
import com.example.hisar.R
import com.example.hisar.api.ApiClient
import com.example.hisar.data.DocJamaah
import com.example.hisar.data.Message
import com.example.hisar.data.PerkabJamaah
import com.example.hisar.data.ReqJadwal
import com.example.hisar.data.RequestId
import com.example.hisar.data.ResRiwayatJamaah
import com.example.hisar.databinding.ActivityProfileJamaah2Binding
import com.example.hisar.databinding.ActivityProfileJamaahBinding
import com.example.hisar.databinding.ConfirmPopupBinding
import com.example.hisar.databinding.ImagePopupBinding
import com.example.hisar.databinding.WarningPopupBinding
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.UUID

@SuppressLint("SetTextI18n")
class ProfileJamaah2 : AppCompatActivity() {

    private lateinit var binding: ActivityProfileJamaah2Binding
    private lateinit var riwayat:RecyclerView


    private fun showWarning(key: String, id: String, text:String, to:String){
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
                "jadwal"->{
                    dialog.dismiss()
                    setJadwal(key,id,binding.dateResult.text.toString())
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
                        val sort: ArrayList<ResRiwayatJamaah.Riwayat> = ArrayList(res.data.sortedByDescending { it.id})
                        riwayat.adapter = DaftarRiwayatAdapter(sort)
                    }
                }

                override fun onFailure(call: Call<ResRiwayatJamaah>, t: Throwable) {
                    Toast.makeText(applicationContext,"Server Error!",Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun setJadwal(key: String,id: String,value: String){
        ApiClient.apiService.jamaahSetJadwal(key, ReqJadwal(id,value))
            .enqueue(object: Callback<Message>{
                override fun onResponse(call: Call<Message>, response: Response<Message>) {
                    if (response.isSuccessful){
                        Toast.makeText(applicationContext,"Sukses Merubah Jadwal!",Toast.LENGTH_SHORT).show()
                        binding.date.visibility = View.GONE
                        binding.riwayat.visibility = View.GONE
                        binding.berangkat.text = value
                        binding.dateResult.text = "Pilih Tanggal"
                        getRiwayat(key,id)
                    }
                }

                override fun onFailure(call: Call<Message>, t: Throwable) {
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

    private fun valid(img:String?,view: LinearLayout,title: String,url: String){
        if (img != null){
            view.visibility = View.VISIBLE
            view.setOnClickListener {
                download(title,url)
            }
        }else{
            view.visibility = View.GONE
        }
    }

    private fun uuid(): String{
        return UUID.randomUUID().toString().substring(1,5)
    }

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val now = LocalDate.now().format(formatter)
    private fun download(title:String,url:String){
        val req = DownloadManager.Request(Uri.parse(url))
            .setTitle("${title}_$now-${uuid()}")
            .setDescription("Downloading...")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setAllowedOverMetered(true)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"${title}_$now-${uuid()}")
        val down = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

        down.enqueue(req)
    }

    private fun getDoc(key: String?,id:String){
        ApiClient.apiService.jamaahDoc(key,RequestId(id))
            .enqueue(object: Callback<DocJamaah>{
                override fun onResponse(call: Call<DocJamaah>, response: Response<DocJamaah>) {
                    if (response.isSuccessful){
                        val res = response.body()!!
                        binding.fotoKtp.isChecked = res.data.ktp?.isNotEmpty() == true
                        binding.fotoKk.isChecked = res.data.kk?.isNotEmpty() == true
                        binding.fotoPaspor.isChecked = res.data.passport?.isNotEmpty() == true
                        binding.foto.isChecked = res.data.foto?.isNotEmpty() == true
                        binding.akteLahir.isChecked = res.data.akteK?.isNotEmpty() == true
                        binding.akteNikah.isChecked = res.data.akteN?.isNotEmpty() == true
                    }
                }

                override fun onFailure(call: Call<DocJamaah>, t: Throwable) {
                    Toast.makeText(applicationContext,"${t.message}",Toast.LENGTH_SHORT).show()
                }

            })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileJamaah2Binding.inflate(layoutInflater)
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
                    View.VISIBLE-> {
                        binding.riwayat.visibility = View.GONE
                        binding.date.visibility = View.GONE
                    }
                    View.GONE ->{
                        binding.riwayat.visibility = View.VISIBLE
                        binding.date.visibility = View.VISIBLE
                    }
                }
            }
        }

        val today = Calendar.getInstance()
        val year = today.get(Calendar.YEAR)
        val month = today.get(Calendar.MONTH)
        val day = today.get(Calendar.DAY_OF_MONTH)

        val date = DatePickerDialog(this,
            { _, p1, p2, p3 -> binding.dateResult.text = String.format("%d-$%d-%d",p1,p2+1,p3).replace("$","0") },year,month,day)

        binding.btnDate.setOnClickListener {
            date.show()
        }

        binding.send.setOnClickListener {
            if (binding.dateResult.text.toString() != "Pilih Tanggal"){
                showWarning(to,intent.getStringExtra("id").toString(),"Ubah Tanggal Berangkat ?","jadwal")
            }else{
                val dialog = Dialog(this)
                val bind = WarningPopupBinding.inflate(layoutInflater)
                dialog.setContentView(bind.root)
                bind.text.text = "Tanggal Tidak Boleh Kosong!"
                bind.no.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.show()
            }
        }


        binding.dp.setOnClickListener{
            showWarning(to,intent.getStringExtra("id").toString(),"Yakin Merubah Status Dp ?","dp")
        }

        intent.getStringExtra("id")?.let { getPerkab(to, it) }
        intent.getStringExtra("id")?.let { getDoc(to, it)}


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

        binding.whatsapp.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW,Uri.parse("https://wa.me/62${intent.getStringExtra("telp")?.substring(1)}"))
            startActivity(intent)
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