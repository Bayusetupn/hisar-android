package com.example.hisar.agen

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.hisar.R
import com.example.hisar.api.ApiClient
import com.example.hisar.data.Message
import com.example.hisar.data.ReqPerkab
import com.example.hisar.data.ReqPerkabEdit
import com.example.hisar.databinding.ActivityEditPerlengkapanJamaahBinding
import com.example.hisar.databinding.SuccesPopupBinding
import com.example.hisar.databinding.WarningPopupBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        val shared = getSharedPreferences("AUTH",Context.MODE_PRIVATE)
        val to:String = shared.getString("KEY",null).toString()

        binding.back.setOnClickListener {
            onBackPressed()
        }

        //old
        binding.koper.isChecked = intens("koper")
        binding.batik.isChecked = intens("batik")
        binding.ihram.isChecked = intens("ihram")
        binding.mukena.isChecked = intens("mukena")
        binding.syal.isChecked = intens("syal")
        binding.panduan.isChecked = intens("panduan")
        binding.doa.isChecked = intens("doa")
        binding.sholawat.isChecked = intens("sholawat")
        binding.peta.isChecked = intens("peta")

        //new
        binding.newKoper.isChecked = intens("koper")
        binding.newBatik.isChecked = intens("batik")
        binding.newIhram.isChecked = intens("ihram")
        binding.newMukena.isChecked = intens("mukena")
        binding.newSyal.isChecked = intens("syal")
        binding.newPanduan.isChecked = intens("panduan")
        binding.newDoa.isChecked = intens("doa")
        binding.newSholawat.isChecked = intens("sholawat")
        binding.newPeta.isChecked = intens("peta")

        binding.editPerkab.setOnClickListener {
            setPerkab(to,intent.getStringExtra("id").toString())
        }

    }

    private fun showSucces(){
        val dialong = Dialog(this)
        val bind = SuccesPopupBinding.inflate(layoutInflater)
        dialong.setContentView(bind.root)
        bind.text.text = "Sukses Edit Perlengkapan"
        bind.no.setOnClickListener {
            onBackPressed()
        }
        dialong.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialong.show()
    }

    private fun showWarning(){
        val dialog = Dialog(this)
        val bind = WarningPopupBinding.inflate(layoutInflater)
        dialog.setContentView(bind.root)
        bind.text.text = "Gagal Edit Perlengkapan"
        bind.no.setOnClickListener {
            dialog.dismiss()
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun setPerkab(key: String,id:String){
        ApiClient.apiService.editPerkab(key, ReqPerkabEdit(id,binding.newSholawat.isChecked,binding.newDoa.isChecked,binding.newIhram.isChecked,binding.newPanduan.isChecked,binding.newMukena.isChecked,binding.newSyal.isChecked,binding.newBatik.isChecked,binding.newKoper.isChecked,binding.newPeta.isChecked))
            .enqueue(object: Callback<Message>{
                override fun onResponse(call: Call<Message>, response: Response<Message>) {
                    if (response.isSuccessful){
                        showSucces()
                    }else{
                        showWarning()
                    }
                }

                override fun onFailure(call: Call<Message>, t: Throwable) {
                    Toast.makeText(applicationContext,"Server Error!",Toast.LENGTH_SHORT).show()
                }

            })
    }

}