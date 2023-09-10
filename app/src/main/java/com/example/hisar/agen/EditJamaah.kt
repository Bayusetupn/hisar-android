package com.example.hisar.agen

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.hisar.R
import com.example.hisar.api.ApiClient
import com.example.hisar.data.Message
import com.example.hisar.data.ReqJamaahEdit
import com.example.hisar.databinding.ActivityEditJamaahBinding
import com.example.hisar.databinding.SuccesPopupBinding
import com.example.hisar.databinding.WarningPopupBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditJamaah : AppCompatActivity() {

    private lateinit var binding: ActivityEditJamaahBinding
    private val jenisKelamin = arrayListOf("Laki-Laki","Perempuan")
    private val paket = arrayListOf("Umroh Premium","Umroh Premium Maulid Nabi","Umroh Plus Turki","Umroh Plus Dubai","Umroh Private","Umroh Family")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditJamaahBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun edittable(text: String?): Editable? {
        return Editable.Factory.getInstance().newEditable(text)
    }

    private fun intens(key:String):String{
        return intent.getStringExtra(key).toString()
    }

    override fun onStart() {
        super.onStart()

        val sharedPreferences = getSharedPreferences("AUTH", Context.MODE_PRIVATE)
        val to:String = sharedPreferences.getString("KEY",null).toString()

        binding.imageView2.setOnClickListener {
            onBackPressed()
        }
        binding.textView2.setOnClickListener {
            onBackPressed()
        }

        binding.nama.text = edittable(intens("nama"))
        binding.noKtp.text = edittable(intens("ktp"))
        binding.noTelepon.text = edittable(intens("telp"))
        binding.alamat.text = edittable(intens("alamat"))
        binding.kelamin.adapter = ArrayAdapter(applicationContext,android.R.layout.simple_spinner_dropdown_item,jenisKelamin)
        binding.kelamin.setSelection(intent.getIntExtra("kelamin",0))
        val defaultValue = paket.indexOf(intens("paket"))
        binding.paket.adapter = ArrayAdapter(applicationContext,android.R.layout.simple_spinner_dropdown_item,paket)
        binding.paket.setSelection(defaultValue)

        binding.edit.setOnClickListener {
            if (isValid()){
                Edit(to)
            }
        }
    }

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

    private fun isValid(): Boolean{
        return if (binding.noKtp.text.isEmpty() || binding.nama.text.isEmpty() || binding.noTelepon.text.isEmpty() || binding.alamat.text.isEmpty()){
            showWarning()
            false
        }else{
            true
        }
    }

    private fun succesDialong(){
        val dialong = Dialog(this)
        val bind = SuccesPopupBinding.inflate(layoutInflater)
        dialong.setContentView(bind.root)
        bind.text.text = "Sukses Edit Jamaah"
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
    private fun Edit(key:String){
        ApiClient.apiService.jamaahEdit(key, ReqJamaahEdit(intens("id"),binding.noKtp.text.toString(),binding.nama.text.toString(),jenisKelamin[binding.kelamin.selectedItemPosition],binding.noTelepon.text.toString(),binding.alamat.text.toString(),paket[binding.paket.selectedItemPosition]))
            .enqueue(object: Callback<Message>{
                override fun onResponse(call: Call<Message>, response: Response<Message>) {
                    if (response.isSuccessful){
                        succesDialong()
                    }
                }

                override fun onFailure(call: Call<Message>, t: Throwable) {
                    Toast.makeText(applicationContext,"Server Error!",Toast.LENGTH_SHORT).show()
                }

            })
    }



}