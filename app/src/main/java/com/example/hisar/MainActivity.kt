package com.example.hisar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.hisar.admin.AdminActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                            saveData(res.to.toString())
                            startActivity(Intent(applicationContext,AdminActivity::class.java))
                            finish()
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

    fun saveData(data:String){
        val sharedPreferences = getSharedPreferences("AUTH",Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.apply {
            putString("KEY",data)
        }.apply()
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        binding = null
//    }


}