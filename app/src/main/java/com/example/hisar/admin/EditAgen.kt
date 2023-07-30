package com.example.hisar.admin

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import com.example.hisar.R
import com.example.hisar.api.ApiClient
import com.example.hisar.data.Message
import com.example.hisar.data.ReqAgenEdit
import com.example.hisar.databinding.ActivityEditAgenBinding
import com.example.hisar.databinding.SuccesPopupBinding
import com.example.hisar.databinding.WarningPopupBinding
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditAgen : AppCompatActivity() {

    private lateinit var binding: ActivityEditAgenBinding

    private fun edittable(text: String?): Editable? {
        return Editable.Factory.getInstance().newEditable(text)
    }

    private fun intents(value:String): String? {
        return intent.getStringExtra(value)
    }

    private fun showWarning(text: String?){
        val dialog = Dialog(this)
        val bind = WarningPopupBinding.inflate(layoutInflater)
        bind.text.text = text
        dialog.setContentView(bind.root)
        bind.no.setOnClickListener {
            dialog.dismiss()
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun showSucces(text: String?){
        val dialog = Dialog(this)
        val bind = SuccesPopupBinding.inflate(layoutInflater)
        dialog.setContentView(bind.root)
        bind.text.text = text
        bind.no.setOnClickListener {
            val intent = Intent(applicationContext,AdminActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

    }
    private fun isValid(): Boolean{
        if (binding.noKtp.text.isEmpty() || binding.nama.text.isEmpty() || binding.noTelepon.text.isEmpty() || binding.alamat.text.isEmpty() || binding.username.text.isEmpty()){
            showWarning("Form Tidak Boleh Kosong!")
            return false
        }else{
            return true
        }
    }

    private fun edit(key:String,
                     id:String?,
                     no_ktp:String?,
                     nama:String?,
                     alamat:String?,
                     no_telepon:String?,
                     role:String?,
                     username:String?){
        ApiClient.apiService.editAgen(key, ReqAgenEdit(id,no_ktp,nama,alamat,no_telepon,role,username))
            .enqueue(object : Callback<Message>{
                override fun onResponse(call: Call<Message>, response: Response<Message>) {
                    if (response.isSuccessful){
                        showSucces("${intents("role").toString().replaceFirstChar { it.uppercase() }} Sukses Di Edit!")
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

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAgenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("AUTH",Context.MODE_PRIVATE)
        val to:String = sharedPreferences.getString("KEY",null).toString()

        binding.title.text = "Edit ${intents("role")}"
        binding.desc.text = "Masukkan Informasi ${intents("role")} Untuk Edit ${intents("role")}"
        binding.noKtp.text = edittable(intents("ktp"))
        binding.nama.text = edittable(intents("nama"))
        binding.alamat.text = edittable(intents("alamat"))
        binding.noTelepon.text = edittable(intents("telp"))
        binding.username.text = edittable(intents("username"))

        binding.textView2.setOnClickListener {
            onBackPressed()
        }
        binding.imageView2.setOnClickListener {
            onBackPressed()
        }

        binding.edit.setOnClickListener {
            isValid().apply {
                if (this){
                    edit(to,intents("id"),binding.noKtp.text.toString(),binding.nama.text.toString(),binding.alamat.text.toString(),binding.noTelepon.text.toString(),intents("role"),binding.username.text.toString())
                }
            }
        }

    }
}