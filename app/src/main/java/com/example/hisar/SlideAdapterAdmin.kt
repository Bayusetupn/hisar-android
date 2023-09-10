package com.example.hisar

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.hisar.admin.PromoAdminActivity
import com.example.hisar.api.ApiClient
import com.example.hisar.data.Banner
import com.example.hisar.data.Message
import com.example.hisar.data.RequestId
import com.example.hisar.databinding.ImageSliderAdminBinding
import com.example.hisar.databinding.ImageSliderBinding
import com.example.hisar.databinding.SuccesPopupBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SlideAdapterAdmin(private val context: Context,private val img : ArrayList<Banner.Item>,private val key:String):
    RecyclerView.Adapter<SlideAdapterAdmin.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideAdapterAdmin.ViewHolder {
        val binding = ImageSliderAdminBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SlideAdapterAdmin.ViewHolder, position: Int) {
        val image = img[position]
        holder.binding.hapus.setOnClickListener {
            image.id?.let { it1 -> deletePromo(it1,holder.itemView.context) }
        }
        Glide.with(holder.itemView.context)
            .load("https://api.hisar.my.id/${image.promo}")
            .placeholder(R.drawable.image_placeholder)
            .into(holder.binding.banner)
    }

    override fun getItemCount(): Int {
        return img.size
    }

    private fun showSucces(context: Context){
        val dialog = Dialog(context)
        val bind = SuccesPopupBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(bind.root)
        bind.text.text = "Sukses hapus promo"
        bind.no.setOnClickListener {
            val intent = Intent(context,PromoAdminActivity::class.java)
                .putExtra("key",key)
            context.startActivity(intent)
            (context as? Activity)?.finish()
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.show()
    }

    inner class ViewHolder(val binding:ImageSliderAdminBinding):RecyclerView.ViewHolder(binding.root){
    }

    private fun deletePromo(id:String,context: Context){
        ApiClient.upload.deletePromo(RequestId(id))
            .enqueue(object: Callback<Message>{
                override fun onResponse(call: Call<Message>, response: Response<Message>) {
                    if (response.isSuccessful){
                        showSucces(context)
                    }
                }

                override fun onFailure(call: Call<Message>, t: Throwable) {
                    Toast.makeText(context,"Server Error!",Toast.LENGTH_SHORT).show()
                }

            })
    }

}