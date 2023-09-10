package com.example.hisar.agen

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hisar.R
import com.example.hisar.api.ApiClient
import com.example.hisar.data.Jamaah
import com.example.hisar.data.Message
import com.example.hisar.data.RequestId
import com.example.hisar.databinding.ActivityAgenProfileBinding
import com.example.hisar.databinding.ImagePopupBinding
import com.example.hisar.databinding.SuccesPopupBinding
import com.example.hisar.login.DaftarJamaahAdapter_Self
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import okhttp3.MediaType
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

class AgenProfile : AppCompatActivity() {
    private lateinit var binding: ActivityAgenProfileBinding
    private lateinit var daftar:RecyclerView

    private fun intents(text:String): String? {
        return intent.getStringExtra(text)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgenProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("AUTH",Context.MODE_PRIVATE)
        val to:String = sharedPreferences.getString("KEY",null).toString()

        daftar = binding.daftar
        daftar.layoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL,false)
        binding.textView2.setOnClickListener {
            onBackPressed()
        }
        binding.imageView2.setOnClickListener {
            onBackPressed()
        }
        binding.profilePic.setOnClickListener {
            val dialog = Dialog(this)
            val bind = ImagePopupBinding.inflate(layoutInflater)
            dialog.setContentView(bind.root)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            bind.img.setImageBitmap((binding.profilePic.drawable as BitmapDrawable).bitmap)
            dialog.show()
        }
        Glide
            .with(applicationContext)
            .load("https://api.hisar.my.id/"+ intents("foto"))
            .placeholder(R.drawable.user)
            .into(binding.profilePic)

        binding.nama.text = intents("nama")
        binding.role.text = intents("role")
        binding.alamat.text = intents("alamat")
        binding.ktp.text = intents("ktp")
        binding.username.text = intents("username")
        binding.bergabung.text = intents("bergabung")
        binding.totalJamaah.text = intent.getIntExtra("total",0).toString()
        binding.telp.text = intents("telp")
        getJamaah(to,intents("id").toString())

        binding.more.setOnClickListener {
            val intent = Intent(applicationContext,AgenActivity::class.java)
                .putExtra("yes","yes")
            startActivity(intent)
            finish()
        }

        binding.edit.setOnClickListener {
            val intent = Intent(applicationContext,EditProfileAgen::class.java)
                .putExtra("id",intents("id"))
                .putExtra("nama",intents("nama"))
                .putExtra("ktp",intents("ktp"))
                .putExtra("username",intents("username"))
                .putExtra("telp",intents("telp"))
                .putExtra("alamat",intents("alamat"))
                .putExtra("role",intents("role"))
            startActivity(intent)
        }

        binding.editProfile.setOnClickListener {
            ImagePicker.with(this).apply {
                this.cropSquare()
                this.galleryMimeTypes(  //Exclude gif images
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
        val dir = this@AgenProfile.cacheDir
        val imgCache = File(dir,name)
        imgCache.delete()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(resultCode){
            RESULT_OK->{
                val fileuri = data?.data!!
                binding.saveProfile.visibility = View.VISIBLE
                binding.profilePic.setImageURI(fileuri)
                binding.saveProfile.setOnClickListener {
                    val filedir = applicationContext.cacheDir
                    val file = File(filedir,"profile.jpg")
                    val inputStream = contentResolver.openInputStream(fileuri)
                    val outputStream = FileOutputStream(file)
                    inputStream?.copyTo(outputStream)
                    inputStream?.close()
                    val newFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                    val imagePart = MultipartBody.Part.createFormData("image",file.name,newFile)

                    val ids = intents("id").toString().toRequestBody("text/plain".toMediaTypeOrNull())
                    val params = HashMap<String,RequestBody>()
                    params["id"] = ids

                    ApiClient.upload.profileUpload(params,imagePart)
                        .enqueue(object: Callback<Message>{
                            override fun onResponse(
                                call: Call<Message>,
                                response: Response<Message>
                            ) {
                                if (response.isSuccessful){
                                    deleteCache("profile.jpg")
                                    showSucces()

                                }
                            }

                            override fun onFailure(call: Call<Message>, t: Throwable) {
                                Toast.makeText(applicationContext,"Server Error!",Toast.LENGTH_SHORT).show()
                            }

                        })

                }
            }
        }
    }

    private fun getJamaah(key:String,id:String){
        ApiClient.apiService.jamaah(key, RequestId(id))
            .enqueue(object: Callback<Jamaah>{
                override fun onResponse(call: Call<Jamaah>, response: Response<Jamaah>) {
                    if (response.isSuccessful){
                        val res = response.body()!!
                        if (res.data.size < 1){
                            binding.load.visibility = View.GONE
                            binding.notFound.visibility = View.VISIBLE
                            daftar.visibility = View.GONE
                        }else{
                            daftar.visibility = View.VISIBLE
                            daftar.adapter = DaftarJamaahAdapter_Self(res.data,2)
                            binding.load.visibility = View.GONE
                            binding.notFound.visibility = View.GONE
                        }
                    }
                }

                override fun onFailure(call: Call<Jamaah>, t: Throwable) {
                    Toast.makeText(applicationContext,"Server Error!",Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun showSucces(){
        val dialog = Dialog(this)
        val bind = SuccesPopupBinding.inflate(layoutInflater)
        bind.text.text = "Sukses Update Profile"
        bind.no.setOnClickListener {
            binding.saveProfile.visibility = View.GONE
            dialog.dismiss()
        }
        dialog.setContentView(bind.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }
}