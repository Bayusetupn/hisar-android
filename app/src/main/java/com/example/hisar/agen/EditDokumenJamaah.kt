package com.example.hisar.agen

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.hisar.R
import com.example.hisar.api.ApiClient
import com.example.hisar.data.DocJamaah
import com.example.hisar.data.Message
import com.example.hisar.data.RequestId
import com.example.hisar.databinding.ActivityEditDokumenJamaahBinding
import com.example.hisar.databinding.ImagePopupBinding
import com.example.hisar.databinding.SuccesPopupBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class EditDokumenJamaah : AppCompatActivity() {

    private var imgOf = "ktp"
    private lateinit var binding:ActivityEditDokumenJamaahBinding
    private var fileUri = ArrayList<Uri>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditDokumenJamaahBinding.inflate(layoutInflater)
        setContentView(binding.root)
        for (i in 0..5){
            fileUri.add(i, Uri.parse("null"))
        }
    }

    private fun getDoc(key: String?,id:String){
        ApiClient.apiService.jamaahDoc(key, RequestId(id))
            .enqueue(object: Callback<DocJamaah> {
                override fun onResponse(call: Call<DocJamaah>, response: Response<DocJamaah>) {
                    if (response.isSuccessful){
//                        val res = response.body()!!
//                        val ktp = Glide
//                            .with(applicationContext)
//                            .load("https://api.hisar.my.id/${res.data.ktp}")
//                            .placeholder(R.drawable.image_placeholder)
//                        ktp.into(binding.fotoKtp)
//                        val kk = Glide
//                            .with(applicationContext)
//                            .load("https://api.hisar.my.id/${res.data.kk}")
//                            .placeholder(R.drawable.image_placeholder)
//                        kk.into(binding.fotoKk)
//                        val akteL = Glide
//                            .with(applicationContext)
//                            .load("https://api.hisar.my.id/${res.data.akteK}")
//                            .placeholder(R.drawable.image_placeholder)
//                        akteL.into(binding.akteLahir)
//                        val akteN = Glide
//                            .with(applicationContext)
//                            .load("https://api.hisar.my.id/${res.data.akteN}")
//                            .placeholder(R.drawable.image_placeholder)
//                        akteN.into(binding.akteNikah)
//                        val foto = Glide
//                            .with(applicationContext)
//                            .load("https://api.hisar.my.id/${res.data.foto}")
//                            .placeholder(R.drawable.image_placeholder)
//                        foto.into(binding.pasFoto)
//                        val paspor = Glide
//                            .with(applicationContext)
//                            .load("https://api.hisar.my.id/${res.data.passport}")
//                            .placeholder(R.drawable.image_placeholder)
//                        paspor.into(binding.fotoPaspor)
                    }
                }

                override fun onFailure(call: Call<DocJamaah>, t: Throwable) {
                    Toast.makeText(applicationContext,"${t.message}",Toast.LENGTH_SHORT).show()
                }

            })
    }
    override fun onStart() {
        super.onStart()

        val sharedPreferences = getSharedPreferences("AUTH",Context.MODE_PRIVATE)
        val to:String = sharedPreferences.getString("KEY",null).toString()

        getDoc(to,intent.getStringExtra("id").toString())

        binding.fotoKtp.setOnClickListener {
            val dialog = Dialog(this)
            val bind = ImagePopupBinding.inflate(layoutInflater)
            dialog.setContentView(bind.root)
            bind.img.setImageDrawable(binding.fotoKtp.drawable)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }

        binding.fotoKk.setOnClickListener {
            val dialog = Dialog(this)
            val bind = ImagePopupBinding.inflate(layoutInflater)
            dialog.setContentView(bind.root)
            bind.img.setImageDrawable(binding.fotoKk.drawable)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }

        binding.akteLahir.setOnClickListener {
            val dialog = Dialog(this)
            val bind = ImagePopupBinding.inflate(layoutInflater)
            dialog.setContentView(bind.root)
            bind.img.setImageDrawable(binding.akteLahir.drawable)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }
        binding.akteNikah.setOnClickListener {
            val dialog = Dialog(this)
            val bind = ImagePopupBinding.inflate(layoutInflater)
            dialog.setContentView(bind.root)
            bind.img.setImageDrawable(binding.akteNikah.drawable)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }
        binding.pasFoto.setOnClickListener {
            val dialog = Dialog(this)
            val bind = ImagePopupBinding.inflate(layoutInflater)
            dialog.setContentView(bind.root)
            bind.img.setImageDrawable(binding.pasFoto.drawable)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }
        binding.fotoPaspor.setOnClickListener {
            val dialog = Dialog(this)
            val bind = ImagePopupBinding.inflate(layoutInflater)
            dialog.setContentView(bind.root)
            bind.img.setImageDrawable(binding.fotoPaspor.drawable)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }

        //new
        binding.newFotoKtp.setOnClickListener {
            val dialog = Dialog(this)
            val bind = ImagePopupBinding.inflate(layoutInflater)
            dialog.setContentView(bind.root)
            bind.img.setImageDrawable(binding.newFotoKtp.drawable)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }

        binding.newFotoKk.setOnClickListener {
            val dialog = Dialog(this)
            val bind = ImagePopupBinding.inflate(layoutInflater)
            dialog.setContentView(bind.root)
            bind.img.setImageDrawable(binding.newFotoKk.drawable)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }

        binding.newAkteLahir.setOnClickListener {
            val dialog = Dialog(this)
            val bind = ImagePopupBinding.inflate(layoutInflater)
            dialog.setContentView(bind.root)
            bind.img.setImageDrawable(binding.newAkteLahir.drawable)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }
        binding.newAkteNikah.setOnClickListener {
            val dialog = Dialog(this)
            val bind = ImagePopupBinding.inflate(layoutInflater)
            dialog.setContentView(bind.root)
            bind.img.setImageDrawable(binding.newAkteNikah.drawable)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }
        binding.newPasFoto.setOnClickListener {
            val dialog = Dialog(this)
            val bind = ImagePopupBinding.inflate(layoutInflater)
            dialog.setContentView(bind.root)
            bind.img.setImageDrawable(binding.newPasFoto.drawable)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }
        binding.newFotoPaspor.setOnClickListener {
            val dialog = Dialog(this)
            val bind = ImagePopupBinding.inflate(layoutInflater)
            dialog.setContentView(bind.root)
            bind.img.setImageDrawable(binding.newFotoPaspor.drawable)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }

        binding.uploadKtp.setOnClickListener {
            imgOf = "ktp"
            ImagePicker.with(this)
                .galleryOnly()
                .galleryMimeTypes(  //Exclude gif images
                    mimeTypes = arrayOf(
                        "image/png",
                        "image/jpg",
                        "image/jpeg"
                    )
                )
                .crop()
                .start()
        }
        binding.deleteKtp.setOnClickListener {
            binding.newFotoKtp.setImageResource(R.drawable.image_placeholder)
        }

        binding.back.setOnClickListener {
            onBackPressed()
        }

        binding.uploadKk.setOnClickListener {
            imgOf = "kk"
            ImagePicker.with(this)
                .galleryOnly()
                .galleryMimeTypes(  //Exclude gif images
                    mimeTypes = arrayOf(
                        "image/png",
                        "image/jpg",
                        "image/jpeg"
                    )
                )
                .crop()
                .start()
        }

        binding.deleteKk.setOnClickListener {
            binding.newFotoKk.setImageResource(R.drawable.image_placeholder)
        }

        binding.uploadFoto.setOnClickListener {
            imgOf = "foto"
            ImagePicker.with(this)
                .galleryOnly()
                .galleryMimeTypes(  //Exclude gif images
                    mimeTypes = arrayOf(
                        "image/png",
                        "image/jpg",
                        "image/jpeg"
                    )
                )
                .crop()
                .start()
        }
        binding.deleteFoto.setOnClickListener {
            binding.newPasFoto.setImageResource(R.drawable.image_placeholder)
        }

        binding.uploadPaspor.setOnClickListener {
            imgOf = "paspor"
            ImagePicker.with(this)
                .galleryOnly()
                .galleryMimeTypes(  //Exclude gif images
                    mimeTypes = arrayOf(
                        "image/png",
                        "image/jpg",
                        "image/jpeg"
                    )
                )
                .crop()
                .start()
        }
        binding.deletePaspor.setOnClickListener {
            binding.newFotoPaspor.setImageResource(R.drawable.image_placeholder)
        }

        binding.uploadAkteNikah.setOnClickListener {
            imgOf = "akteN"
            ImagePicker.with(this)
                .galleryOnly()
                .galleryMimeTypes(  //Exclude gif images
                    mimeTypes = arrayOf(
                        "image/png",
                        "image/jpg",
                        "image/jpeg"
                    )
                )
                .crop()
                .start()
        }
        binding.deleteAkteNikah.setOnClickListener {
            binding.newAkteNikah.setImageResource(R.drawable.image_placeholder)
        }

        binding.uploadAkteLahir.setOnClickListener {
            imgOf = "akteL"
            ImagePicker.with(this)
                .galleryOnly()
                .galleryMimeTypes(  //Exclude gif images
                    mimeTypes = arrayOf(
                        "image/png",
                        "image/jpg",
                        "image/jpeg"
                    )
                )
                .crop()
                .start()
        }
        binding.deleteAkteLahir.setOnClickListener {
            binding.newAkteLahir.setImageResource(R.drawable.image_placeholder)
        }
    }


    private fun deleteCache(name:String){
        val dir = this@EditDokumenJamaah.cacheDir
        val imgCache = File(dir,name)
        imgCache.delete()
    }

    @SuppressLint("SetTextI18n")
    private fun showSucces(){
        val dialog = Dialog(this)
        val bind = SuccesPopupBinding.inflate(layoutInflater)
        dialog.setContentView(bind.root)
        bind.text.text = "Sukses Edit Dokumen"
        bind.no.setOnClickListener {
            onBackPressed()
        }
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val ids = intent.getStringExtra("id").toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val params = HashMap<String, RequestBody>()
        params["id"] = ids

        fun files(i:Int ,fileName:String,key:String): MultipartBody.Part? {
            return if (fileUri[i].path == Uri.parse("null").path){
                null
            }else{
                val filedir = applicationContext.cacheDir
                val file = File(filedir,fileName)
                val inputStream = contentResolver.openInputStream(fileUri[i])
                val outputStream = FileOutputStream(file)
                inputStream?.copyTo(outputStream)
                inputStream?.close()
                val newFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                return MultipartBody.Part.createFormData(key,file.name,newFile)
            }
        }

        when (resultCode) {
            Activity.RESULT_OK -> {
                when(imgOf){
                    "ktp"->{
                        fileUri[0] = data?.data!!
                        binding.newFotoKtp.setImageURI(fileUri[0])
                    }
                    "kk"->{
                        fileUri[1] = data?.data!!
                        binding.newFotoKk.setImageURI(fileUri[1])
                    }
                    "foto"->{
                        fileUri[2] = data?.data!!
                        binding.newPasFoto.setImageURI(fileUri[2])
                    }
                    "paspor"->{
                        fileUri[3] = data?.data!!
                        binding.newFotoPaspor.setImageURI(fileUri[3])
                    }
                    "akteN"->{
                        fileUri[4] = data?.data!!
                        binding.newAkteNikah.setImageURI(fileUri[4])
                    }
                    "akteL"->{
                        fileUri[5] = data?.data!!
                        binding.newAkteLahir.setImageURI(fileUri[5])
                    }
                }


                binding.save.setOnClickListener {
                    for (i in fileUri){
                        Log.d("kocak",i.toString())
                    }
                    try {
                        ApiClient.upload.docEdit(params,
                            files(0,"ktp.jpg","ktp"),
                            files(1,"kk.jpg","kk"),
                            files(3,"paspor.jpg","paspor"),
                            files(2,"foto.jpg","foto"),
                            files(5,"akteK.jpg","akteK"),
                            files(4,"akteN.jpg","akteN"))
                            .enqueue(object : Callback<Message>{
                                override fun onResponse(
                                    call: Call<Message>,
                                    response: Response<Message>
                                ) {
                                    if (response.isSuccessful){
                                        deleteCache("ktp")
                                        deleteCache("kk")
                                        deleteCache("paspor")
                                        deleteCache("foto")
                                        deleteCache("akteK")
                                        deleteCache("akteN")
                                        showSucces()
                                    }else{
                                        val res = Gson().fromJson(response.errorBody()?.string(),Message::class.java)
                                        Toast.makeText(applicationContext,"${res.message}",Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onFailure(call: Call<Message>, t: Throwable) {
                                    Toast.makeText(applicationContext,"Server Error! ${t.message}",Toast.LENGTH_SHORT).show()
                                }

                            })
                    }catch (e:Exception){
                        Toast.makeText(applicationContext,e.message.toString(),Toast.LENGTH_SHORT).show()
                    }
                }

            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "Tidak ada foto yang dipilih", Toast.LENGTH_SHORT).show()
            }
        }
    }

}