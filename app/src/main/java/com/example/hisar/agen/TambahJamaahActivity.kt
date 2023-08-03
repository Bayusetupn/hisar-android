package com.example.hisar.agen

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isEmpty
import androidx.core.widget.addTextChangedListener
import com.example.hisar.api.ApiClient
import com.example.hisar.data.Kota
import com.example.hisar.data.Message
import com.example.hisar.data.Provinsi
import com.example.hisar.data.ReqJamaah
import com.example.hisar.databinding.ActivityTambahJamaahBinding
import com.example.hisar.databinding.SuccesPopupBinding
import com.example.hisar.databinding.WarningPopupBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("SetTextI18n")
class TambahJamaahActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTambahJamaahBinding
    private var data: List<Provinsi.ProvinsiItem> = emptyList()
    private var kota: List<Kota.Kabupaten> = emptyList()
    private var alamat: ArrayList<String> = arrayListOf()
    private val jenisKelamin = arrayListOf("Laki-Laki","Perempuan")
    private val paket = arrayListOf("Umroh Premium","Umroh Premium Maulid Nabi","Umroh Plus Turki","Umroh Plus Dubai","Umroh Private","Umroh Family")

    private fun showWarning(){
        val dialong = Dialog(this)
        val bind = WarningPopupBinding.inflate(layoutInflater)
        dialong.setContentView(bind.root)
        bind.text.text = "Form Tidak Boleh Kosong"
        bind.no.setOnClickListener {
            dialong.dismiss()
        }
        dialong.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialong.show()
    }

    private fun succesDialong(){
        val dialong = Dialog(this)
        val bind = SuccesPopupBinding.inflate(layoutInflater)
        dialong.setContentView(bind.root)
        bind.text.text = "Sukses Menambah Jamaah"
        bind.no.setOnClickListener {
            startActivity(
                Intent(applicationContext, AgenActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            )
            finish()
        }
        dialong.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialong.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     binding = ActivityTambahJamaahBinding.inflate(layoutInflater)
     setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("AUTH", Context.MODE_PRIVATE)
        val to:String = sharedPreferences.getString("KEY",null).toString()

        for (i in 0..7){
            alamat.add(i,"")
        }

        getProvinsi()

        binding.textView2.setOnClickListener {
            onBackPressed()
        }
        binding.imageView2.setOnClickListener {
            onBackPressed()
        }

        binding.kecamatan.addTextChangedListener {
            alamat[2] = "Kecamatan $it,"
            binding.full.text = "${alamat[6]} ${alamat[5]} ${alamat[4]} ${alamat[3]} ${alamat[2]} ${alamat[1]} ${alamat[0]}"
        }
        binding.kelurahan.addTextChangedListener {
            alamat[3] = "$it,"
            binding.full.text = "${alamat[6]} ${alamat[5]} ${alamat[4]} ${alamat[3]} ${alamat[2]} ${alamat[1]} ${alamat[0]}"
        }
        binding.rw.addTextChangedListener {
            alamat[4] = "Rw $it,"
            binding.full.text = "${alamat[6]} ${alamat[5]} ${alamat[4]} ${alamat[3]} ${alamat[2]} ${alamat[1]} ${alamat[0]}"
        }
        binding.rt.addTextChangedListener {
            alamat[5] = "Rt $it/"
            binding.full.text = "${alamat[6]} ${alamat[5]} ${alamat[4]} ${alamat[3]} ${alamat[2]} ${alamat[1]} ${alamat[0]}"
        }
        binding.pendukung.addTextChangedListener {
            alamat[6] = "$it,"
            binding.full.text = "${alamat[6]} ${alamat[5]} ${alamat[4]} ${alamat[3]} ${alamat[2]} ${alamat[1]} ${alamat[0]}"
        }

        binding.tambah.setOnClickListener {
            if (isValid()){
                addJamaah(to)
            }
        }

        binding.kelamin.adapter = ArrayAdapter(applicationContext,android.R.layout.simple_spinner_dropdown_item,jenisKelamin)


        binding.paket.adapter = ArrayAdapter(applicationContext,android.R.layout.simple_spinner_dropdown_item,paket)

    }

    private fun setSpinnerData(){
        val item = data.map { it.nama }
        val adapter = ArrayAdapter(applicationContext,android.R.layout.simple_spinner_dropdown_item,item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.provinsi.adapter = adapter

    }

    private fun setSpinnerKota(){
        val item = kota.map { it.nama }
        val adapter = ArrayAdapter(applicationContext,android.R.layout.simple_spinner_dropdown_item,item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.kota.adapter = adapter
    }

    private fun getKota(id: Int){
        ApiClient.alamat.getKota(id)
            .enqueue(object : Callback<Kota> {
                override fun onResponse(call: Call<Kota>, response: Response<Kota>) {
                    if (response.isSuccessful){
                        kota = response.body()?.kotaKabupaten?: Kota.Kabupaten("Pilih Kabupaten/Kota") as List<Kota.Kabupaten>
                        setSpinnerKota()
                        binding.kota.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                            override fun onItemSelected(
                                p0: AdapterView<*>?,
                                p1: View?,
                                p2: Int,
                                p3: Long
                            ) {
                                alamat[1] =  "${p0?.getItemAtPosition(p2)},"
                                binding.full.text = "${alamat[6]} ${alamat[5]} ${alamat[4]} ${alamat[3]} ${alamat[2]} ${alamat[1]} ${alamat[0]}"
                            }

                            override fun onNothingSelected(p0: AdapterView<*>?) {

                            }

                        }
                    }
                }

                override fun onFailure(call: Call<Kota>, t: Throwable) {
                    Toast.makeText(applicationContext,"Server Error!", Toast.LENGTH_SHORT).show()
                }

            })
    }
    private fun getProvinsi(){
        ApiClient.alamat.getProvinsi()
            .enqueue(object: Callback<Provinsi> {
                override fun onResponse(call: Call<Provinsi>, response: Response<Provinsi>) {
                    if (response.isSuccessful){
                        data = (response.body()?.provinsi ?: Provinsi.ProvinsiItem("Pilih Provinsi")) as List<Provinsi.ProvinsiItem>
                        setSpinnerData()
                        binding.provinsi.onItemSelectedListener= object: AdapterView.OnItemSelectedListener{
                            override fun onItemSelected(
                                p0: AdapterView<*>?,
                                p1: View?,
                                p2: Int,
                                p3: Long
                            ) {
                                binding.full.text = "${alamat[6]} ${alamat[5]} ${alamat[4]} ${alamat[3]} ${alamat[2]} ${alamat[1]} ${alamat[0]}"
                                data[p2].nama?.let { alamat.set(0, "$it.") }
                                data[p2].id?.let { getKota(it) }
                            }

                            override fun onNothingSelected(p0: AdapterView<*>?) {

                            }
                        }
                    }

                }

                override fun onFailure(call: Call<Provinsi>, t: Throwable) {
                    Toast.makeText(applicationContext,"Server Error!", Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun addJamaah(key:String){
        ApiClient.apiService.jamaahTambah(key,
            ReqJamaah(binding.noKtp.text.toString(),binding.nama.text.toString(),jenisKelamin[binding.kelamin.selectedItemPosition],binding.noTelepon.text.toString(),binding.full.text.toString(),false,paket[binding.paket.selectedItemPosition],intent.getStringExtra("id").toString())
        )
            .enqueue(object: Callback<Message>{
                override fun onResponse(call: Call<Message>, response: Response<Message>) {
                    response.body()!!
                    if (response.isSuccessful){
                        succesDialong()
                    }
                }

                override fun onFailure(call: Call<Message>, t: Throwable) {
                    Toast.makeText(applicationContext,"Server Error!",Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun isValid(): Boolean{
        return if (binding.noKtp.text.isEmpty() || binding.nama.text.isEmpty() || binding.kelamin.isEmpty() || binding.noTelepon.text.isEmpty() || binding.full.text.isEmpty() || binding.paket.isEmpty()){
            showWarning()
            false
        }else{
            true
        }
    }

}