package com.example.hisar.agen

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.hisar.R
import com.example.hisar.api.ApiClient
import com.example.hisar.data.Message
import com.example.hisar.data.RequestId
import com.example.hisar.databinding.ActivityAgenBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AgenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgenBinding
    private lateinit var id:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("AUTH",Context.MODE_PRIVATE)
        val to:String = sharedPreferences.getString("KEY",null).toString()
        val role:String = sharedPreferences.getString("ROLE",null).toString()
        id = sharedPreferences.getString("ID",null).toString()

        getID(to)

        frag(AgenDash.setData(to,role,id))
        binding.adminNav.selectedItemId = R.id.agen_dash

        binding.adminNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.agen_dash-> {
                    frag(AgenDash.setData(to,role,id))
                    binding.adminNav.menu.findItem(R.id.agen_dash).isEnabled = false
                    binding.adminNav.menu.findItem(R.id.agen_jamaah).isEnabled = true
                    binding.adminNav.menu.findItem(R.id.agen_agen).isEnabled = true
                }
                R.id.agen_agen->{
                    frag(AgenDaftar.setData(to,role))
                    binding.adminNav.menu.findItem(R.id.agen_dash).isEnabled = true
                    binding.adminNav.menu.findItem(R.id.agen_jamaah).isEnabled = true
                    binding.adminNav.menu.findItem(R.id.agen_agen).isEnabled = false
                }
                R.id.agen_jamaah->{
                    frag(JamaahAnda.setData(to,role,id))
                    binding.adminNav.menu.findItem(R.id.agen_dash).isEnabled = true
                    binding.adminNav.menu.findItem(R.id.agen_jamaah).isEnabled = false
                    binding.adminNav.menu.findItem(R.id.agen_agen).isEnabled = true
                }
                else->{

                }
            }
            true
        }


    }

    private fun getID(key:String){
        ApiClient.apiService.Key(RequestId(key))
            .enqueue(object: Callback<Message>{
                override fun onResponse(call: Call<Message>, response: Response<Message>) {
                    if (response.isSuccessful){
                        val res = response.body()!!
                        val sharedPreferences = getSharedPreferences("AUTH",Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.apply {
                            this.putString("ID",res.message)
                            editor.apply()
                        }
                        id = sharedPreferences.getString("ID",null).toString()
                    }
                }

                override fun onFailure(call: Call<Message>, t: Throwable) {
                    Toast.makeText(applicationContext,"ID NOT FOUND",Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun frag(frag: Fragment){
        val supportFragment = supportFragmentManager
        val frags = supportFragment.beginTransaction()
        frags.replace(R.id.frag,frag)
        frags.commit()
    }
}