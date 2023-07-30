package com.example.hisar.admin
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
import androidx.core.widget.addTextChangedListener
import com.example.hisar.api.ApiClient
import com.example.hisar.api.ApiServices
import com.example.hisar.data.Kota
import com.example.hisar.data.Message
import com.example.hisar.data.Provinsi
import com.example.hisar.data.ReqCreate
import com.example.hisar.databinding.ActivityTambahAgenBinding
import com.example.hisar.databinding.SuccesPopupBinding
import com.example.hisar.databinding.WarningPopupBinding
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
@SuppressLint("SetTextI18n")
class TambahAgenAcitvity : AppCompatActivity() {

    private lateinit var binding: ActivityTambahAgenBinding
    private var data: List<Provinsi.ProvinsiItem> = emptyList()
    private var kota: List<Kota.Kabupaten> = emptyList()
    private var alamat: ArrayList<String> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahAgenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("AUTH",Context.MODE_PRIVATE)
        val to:String = sharedPreferences.getString("KEY",null).toString()

        getProvinsi()

        binding.title.text = intent.getStringExtra("title")
        binding.desc.text = intent.getStringExtra("desc")

        for (i in 0..7){
            alamat.add(i,"")
        }

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
            isValid().apply {
                if (this){
                    onSubmit(to)
                }
            }
        }

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
            .enqueue(object : Callback<Kota>{
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
                    Toast.makeText(applicationContext,"Server Error!",Toast.LENGTH_SHORT).show()
                }

            })
    }
    private fun getProvinsi(){
        ApiClient.alamat.getProvinsi()
            .enqueue(object: Callback<Provinsi>{
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
                    Toast.makeText(applicationContext,"Server Error!",Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun onSubmit(key: String){
        ApiClient.apiService.tambah(key,ReqCreate(binding.noKtp.text.toString(),binding.nama.text.toString(),binding.full.text.toString(),binding.noTelepon.text.toString(),intent.getStringExtra("role"),binding.username.text.toString(),binding.password.text.toString()))
            .enqueue(object : Callback<Message>{
                override fun onResponse(call: Call<Message>, response: Response<Message>) {
                    if (response.isSuccessful){
                            succesDialong("${intent.getStringExtra("role").toString().replaceFirstChar { 
                                it.uppercase()
                            }} Sukses Dibuat!")
                    }else{
                        val res = Gson().fromJson(response.errorBody()?.string(),Message::class.java)
                        showWarning(res.message.toString())
                    }
                }

                override fun onFailure(call: Call<Message>, t: Throwable) {
                    Toast.makeText(applicationContext,"Server Error!",Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun showWarning(text: String){
        val dialong = Dialog(this)
        val bind = WarningPopupBinding.inflate(layoutInflater)
        dialong.setContentView(bind.root)
        bind.text.text = text
        bind.no.setOnClickListener {
            dialong.dismiss()
        }
        dialong.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialong.show()
    }

    private fun succesDialong(text: String){
        val dialong = Dialog(this)
        val bind = SuccesPopupBinding.inflate(layoutInflater)
        dialong.setContentView(bind.root)
        bind.text.text = text
        bind.no.setOnClickListener {
            startActivity(Intent(applicationContext,AdminActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            )
            finish()
        }
        dialong.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialong.show()
    }

    private fun isValid(): Boolean{
        val form = binding
        if (form.noKtp.text.isEmpty() || form.nama.text.isEmpty() || form.noTelepon.text.isEmpty() || form.full.text.isEmpty() || form.username.text.isEmpty() || form.password.text.isEmpty() || form.konfirPassword.text.isEmpty() ){
            showWarning("Form Tidak Boleh Kosong!")
            return false
        }else if (form.password.text.length < 8){
            showWarning("Password Minimal 8 Karakter!")
            return false
        }else if(form.password.text.toString() != form.konfirPassword.text.toString()){
            showWarning("Password dan Konfirmasi Password Harus Sama!")
            return false
        }else{
            return true
        }
    }

}
