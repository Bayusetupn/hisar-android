package com.example.hisar

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.Manifest
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.example.hisar.admin.AdminActivity
import com.example.hisar.agen.AgenActivity
import com.example.hisar.api.ApiClient
import com.example.hisar.data.Data
import com.example.hisar.databinding.ActivityMainBinding
import com.example.hisar.login.LoginRequest
import com.example.hisar.login.LoginResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    private val STORAGE_PERMISSION_CODE = 1

    private fun isPermissionGranted(): Boolean {
        val readPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        val writePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return readPermission == PackageManager.PERMISSION_GRANTED && writePermission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
            STORAGE_PERMISSION_CODE)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

       when(isPermissionGranted()){
           false->{
               requestPermission()
           }
           else -> {}
       }


        binding.username.addTextChangedListener {
            binding.error.visibility = View.GONE
        }
        binding.password.addTextChangedListener {
            binding.error.visibility = View.GONE
        }

        binding.loginBtn.setOnClickListener{
            binding.text.visibility = View.GONE
            binding.load.visibility = View.VISIBLE
            val data = LoginRequest(binding.username.text.toString(),binding.password.text.toString())
            ApiClient.apiService.login(data)
                .enqueue(object: Callback<LoginResponse>{
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if (response.isSuccessful){
                            val res = response.body()!!
                            if (res.role == "admin" || res.role == "manager"){
                                saveData(res.to.toString(),res.role)
                                startActivity(Intent(applicationContext,AdminActivity::class.java))
                                finish()
                            }else if(res.role == "agen" || res.role == "ustad"){
                                saveData(res.to.toString(),res.role)
                                startActivity(Intent(applicationContext,AgenActivity::class.java))
                                finish()
                            }
                        }else{
                            val res = Gson().fromJson(response.errorBody()?.string(),LoginResponse::class.java)
                            binding.error.visibility = View.VISIBLE
                            binding.error.text = "${res.to}"
                            binding.text.visibility = View.VISIBLE
                            binding.load.visibility = View.GONE
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        binding.error.visibility = View.VISIBLE
                        binding.error.text = "Server Error !"
                        binding.text.visibility = View.VISIBLE
                        binding.load.visibility = View.GONE
                    }

                })
        }

    }

    fun saveData(data:String,role:String){
        val sharedPreferences = getSharedPreferences("AUTH",Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.apply {
            putString("KEY",data)
            putString("ROLE",role)
        }.apply()
    }


//    override fun onDestroy() {
//        super.onDestroy()
//        binding = null
//    }


}