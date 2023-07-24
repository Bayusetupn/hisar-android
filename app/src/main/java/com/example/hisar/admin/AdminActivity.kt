package com.example.hisar.admin

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.hisar.R
import com.example.hisar.databinding.ActivityAdminBinding

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("AUTH", Context.MODE_PRIVATE)
        val to:String = sharedPreferences.getString("KEY",null).toString()
        binding.adminNav.selectedItemId = R.id.dash


        frag(Admin_dash.setData(to))

        binding.adminNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.dash->{
                    frag(Admin_dash.setData(to))
                    binding.adminNav.menu.findItem(R.id.dash).isEnabled = false
                    binding.adminNav.menu.findItem(R.id.agen).isEnabled = true
                    binding.adminNav.menu.findItem(R.id.ustad).isEnabled = true
                }
                R.id.agen->{
                    frag(DaftarAgenAdmin())
                    binding.adminNav.menu.findItem(R.id.dash).isEnabled = true
                    binding.adminNav.menu.findItem(R.id.agen).isEnabled = false
                    binding.adminNav.menu.findItem(R.id.ustad).isEnabled = true
                }
                R.id.ustad->{
                    frag((DaftarUstadAdmin()))
                    binding.adminNav.menu.findItem(R.id.dash).isEnabled = true
                    binding.adminNav.menu.findItem(R.id.agen).isEnabled = true
                    binding.adminNav.menu.findItem(R.id.ustad).isEnabled = false
                }
                else -> {

                }
            }
            true
        }

    }

    private fun frag(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frag,fragment)
        fragmentTransaction.commit()
    }

}