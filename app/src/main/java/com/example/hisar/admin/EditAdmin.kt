package com.example.hisar.admin

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.hisar.MainActivity
import com.example.hisar.R
import com.example.hisar.api.ApiClient
import com.example.hisar.data.Message
import com.example.hisar.data.RequestEditAdmin
import com.example.hisar.data.RequestPassword
import com.example.hisar.databinding.ActivityEditAdminBinding
import com.example.hisar.databinding.WarningPopupBinding
import com.example.hisar.login.LoginResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditAdmin : AppCompatActivity() {

    private lateinit var binding:ActivityEditAdminBinding

    private fun edit(key:String?,name: String,username:String,role:String){
        ApiClient.apiService.editAdmin(key,RequestEditAdmin(role,name,username))
            .enqueue(object: Callback<Message>{
                override fun onResponse(call: Call<Message>, response: Response<Message>) {
                    if (response.isSuccessful){
                        Toast.makeText(applicationContext,"Berhasil Edit $role",Toast.LENGTH_SHORT).show()
                        startActivity(Intent(applicationContext,AdminActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        )
                        finish()
                    }else{
                        val res = Gson().fromJson(response.errorBody()?.string(), Message::class.java)
                        binding.error.text = res.message
                        binding.error.visibility = View.VISIBLE
                    }
                }

                override fun onFailure(call: Call<Message>, t: Throwable) {
                    Toast.makeText(applicationContext,"Server Error",Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun editPass(key: String?,newPassword: String,role:String){
        if (binding.password.text.length < 8){
            binding.error2.visibility = View.VISIBLE
            binding.error2.text = "Minimal 8 Karakter!"
        }else if(binding.password.text.toString() != binding.konfirPassword.text.toString()){
            binding.error2.visibility = View.VISIBLE
            binding.error2.text = "Password dan Konfirmasi Password Harus Sama!"
        }else if(binding.password.text.toString() == null && binding.konfirPassword.text.toString() == null){
            binding.error2.visibility = View.VISIBLE
            binding.error.text = "Form Tidak Boleh Kosong!"
        }else if (binding.password.text.toString() == binding.konfirPassword.text.toString()){
            ApiClient.apiService.editPassAdmin(key, RequestPassword(role,newPassword))
                .enqueue(object : Callback<Message>{
                    override fun onResponse(call: Call<Message>, response: Response<Message>) {
                        if (response.isSuccessful){
                            Toast.makeText(applicationContext,"Berhasil Ganti Password, Harap Login Ulang",Toast.LENGTH_SHORT).show()
                            startActivity(Intent(applicationContext,MainActivity::class.java)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            )
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<Message>, t: Throwable) {
                        Toast.makeText(applicationContext,"Server Error!",Toast.LENGTH_SHORT).show()
                    }

                })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val shared = getSharedPreferences("AUTH",Context.MODE_PRIVATE)
        val to:String? = shared.getString("KEY",null)
        val role:String = shared.getString("ROLE",null).toString()

        binding.nama.text = Editable.Factory.getInstance().newEditable(intent.getStringExtra("nama"))
        binding.username.text = Editable.Factory.getInstance().newEditable(intent.getStringExtra("username"))

        binding.nama.addTextChangedListener {
            binding.error.visibility = View.GONE
        }
        binding.username.addTextChangedListener {
            binding.error.visibility = View.GONE
        }

        binding.password.addTextChangedListener {
            binding.error2.visibility = View.GONE
        }
        binding.konfirPassword.addTextChangedListener {
            binding.error2.visibility = View.GONE
        }

        binding.edit.setOnClickListener {
            if (binding.nama.text.isNotEmpty() && binding.username.text.isNotEmpty() ){
                edit(to,binding.nama.text.toString(),binding.username.text.toString(),role)
            }else{
                val dialog = Dialog(this)
                val bind = WarningPopupBinding.inflate(layoutInflater)
                bind.text.text = "Form Tidak Boleh Kosong!"
                dialog.setContentView(bind.root)
                bind.no.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.show()
            }
        }

        binding.changePassword.setOnClickListener {
            editPass(to,binding.password.text.toString(),role)
        }

        binding.textView2.setOnClickListener {
            onBackPressed()
        }
        binding.imageView2.setOnClickListener {
            onBackPressed()
        }

    }
}