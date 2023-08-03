package com.example.hisar.agen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hisar.R
import com.example.hisar.databinding.ActivityEditPerlengkapanJamaahBinding

class EditPerlengkapanJamaah : AppCompatActivity() {

    private lateinit var binding: ActivityEditPerlengkapanJamaahBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPerlengkapanJamaahBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun intens(key:String): Boolean{
        return intent.getBooleanExtra(key,false)
    }

    override fun onStart() {
        super.onStart()

        binding.back.setOnClickListener {
            onBackPressed()
        }

        binding.koper.isChecked = intens("koper")
        binding.batik.isChecked = intens("batik")
        binding.ihram.isChecked = intens("ihram")
        binding.mukena.isChecked = intens("mukena")
        binding.syal.isChecked = intens("syal")
        binding.panduan.isChecked = intens("panduan")
        binding.doa.isChecked = intens("doa")
        binding.sholawat.isChecked = intens("sholawat")
        binding.peta.isChecked = intens("peta")

        binding.newKoper.apply {
            this.isChecked = intens("koper")
            this.setOnClickListener {
                when(this.isSelected){
                    true->this.isSelected = false
                    false->this.isSelected = true
                }
            }
        }

        binding.newBatik.apply {
            this.isChecked = intens("batik")
            this.setOnClickListener {
                when(this.isSelected){
                    true->this.isSelected = false
                    false->this.isSelected = true
                }
            }
        }
        binding.newIhram.apply {
            this.isChecked = intens("ihram")
            this.setOnClickListener {
                when(this.isSelected){
                    true->this.isSelected = false
                    false->this.isSelected = true
                }
            }
        }
        binding.newMukena.apply {
            this.isChecked = intens("mukena")
            this.setOnClickListener {
                when(this.isSelected){
                    true->this.isSelected = false
                    false->this.isSelected = true
                }
            }
        }
        binding.newSyal.apply {
            this.isChecked = intens("syal")
            this.setOnClickListener {
                when(this.isSelected){
                    true->this.isSelected = false
                    false->this.isSelected = true
                }
            }
        }
        binding.newPanduan.apply {
            this.isChecked = intens("panduan")
            this.setOnClickListener {
                when(this.isSelected){
                    true->this.isSelected = false
                    false->this.isSelected = true
                }
            }
        }
        binding.newDoa.apply {
            this.isChecked = intens("doa")
            this.setOnClickListener {
                when(this.isSelected){
                    true->this.isSelected = false
                    false->this.isSelected = true
                }
            }
        }
        binding.newSholawat.apply {
            this.isChecked = intens("sholawat")
            this.setOnClickListener {
                when(this.isSelected){
                    true->this.isSelected = false
                    false->this.isSelected = true
                }
            }
        }
        binding.newPeta.apply {
            this.isChecked = intens("peta")
            this.setOnClickListener {
                when(this.isSelected){
                    true->this.isSelected = false
                    false->this.isSelected = true
                }
            }
        }


    }
}