package com.example.hisar.admin

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.hisar.SlideAdapterAdmin
import com.example.hisar.api.ApiClient
import com.example.hisar.data.Banner
import com.example.hisar.data.Message
import com.example.hisar.databinding.ActivityPromoAdminBinding
import com.example.hisar.databinding.SuccesPopupBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class PromoAdminActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPromoAdminBinding
    private lateinit var adapter: SlideAdapterAdmin
    private lateinit var daftarPromo: ViewPager2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPromoAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupTransformer(){
        val transformer  = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer{page,position->
            run {
                val r = 1 - kotlin.math.abs(position)
                page.scaleY = 0.85f + r * 0.14f
            }
        }

        daftarPromo.setPageTransformer(transformer)
    }
    override fun onStart() {
        super.onStart()

        binding.sendPromo.visibility = View.GONE

        daftarPromo = binding.daftarPromo
        daftarPromo.clipChildren = false
        daftarPromo.clipToPadding = false
        daftarPromo.offscreenPageLimit = 3
        setupTransformer()
        getPromo(intent.getStringExtra("key").toString())

        binding.uploadPromo.setOnClickListener {
            ImagePicker.with(this).apply {
                this.galleryOnly()
                this.galleryMimeTypes(
                    mimeTypes = arrayOf(
                        "image/png",
                        "image/jpg",
                        "image/jpeg"
                    )
                )
                this.compress(1528)
                this.start()
            }

        }


    }

    private fun deleteCache(name:String){
        val dir = this@PromoAdminActivity.cacheDir
        val imgCache = File(dir,name)
        imgCache.delete()
    }

    private fun showSucces(){
        val dialog = Dialog(this)
        val bind = SuccesPopupBinding.inflate(layoutInflater)
        dialog.setContentView(bind.root)
        dialog.setCancelable(false)
        bind.text.text = "Sukses Menambahkan Promo"
        bind.no.setOnClickListener {
            dialog.dismiss()
            getPromo(intent.getStringExtra("key").toString())
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            RESULT_OK -> {
                val fileuri = data?.data!!
                binding.sendPromo.visibility = View.VISIBLE
                binding.banner.setImageURI(fileuri)
                binding.sendPromo.setOnClickListener {
                    binding.load.visibility = View.VISIBLE
                    binding.cek.visibility = View.GONE
                    val filedir = applicationContext.cacheDir
                    val file = File(filedir,"promo.jpg")
                    val inputStream = contentResolver.openInputStream(fileuri)
                    val outputStream = FileOutputStream(file)
                    inputStream?.copyTo(outputStream)
                    inputStream?.close()
                    val newFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                    val imagePart = MultipartBody.Part.createFormData("promo",file.name,newFile)

                    ApiClient.upload.uploadPromo(imagePart)
                        .enqueue(object: Callback<Message>{
                            override fun onResponse(
                                call: Call<Message>,
                                response: Response<Message>
                            ) {
                                if (response.isSuccessful){
                                    deleteCache("promo.jpg")
                                    showSucces()
                                }
                            }

                            override fun onFailure(call: Call<Message>, t: Throwable) {
                                Toast.makeText(applicationContext,"Server Error!",Toast.LENGTH_SHORT).show()
                            }

                        })

                }
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "Tidak ada pilihan", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun getPromo(key:String){
        ApiClient.apiService.getPromo(key)
            .enqueue(object : Callback<Banner>{
                override fun onResponse(call: Call<Banner>, response: Response<Banner>) {
                    if (response.isSuccessful){
                        val res = response.body()!!
                        adapter = SlideAdapterAdmin(this@PromoAdminActivity,res.data,intent.getStringExtra("key").toString())
                        daftarPromo.adapter = adapter
                        binding.load.visibility = View.GONE
                        binding.cek.visibility = View.VISIBLE
                    }
                }

                override fun onFailure(call: Call<Banner>, t: Throwable) {
                    Toast.makeText(applicationContext,"Server Error!",Toast.LENGTH_SHORT).show()
                }

            })
    }

}