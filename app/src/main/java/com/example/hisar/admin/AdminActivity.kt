package com.example.hisar.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.hisar.R
import com.example.hisar.databinding.ActivityAdminBinding

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.adminNav.selectedItemId = R.id.dash
        frag(Admin_dash())

        binding.adminNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.dash->frag(Admin_dash())
                R.id.agen->frag(DaftarAgenAdmin())
                R.id.ustad->frag((DaftarUstadAdmin()))
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