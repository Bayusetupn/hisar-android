package com.example.hisar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hisar.data.Banner
import com.example.hisar.databinding.PromoBinding

class PromoAdapter(private val promo: ArrayList<Banner.Item>):RecyclerView.Adapter<PromoAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromoAdapter.ViewHolder {
        val binding = PromoBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PromoAdapter.ViewHolder, position: Int) {
        val img = promo[position]
        Glide.with(holder.itemView.context)
            .load("https://api.hisar.my.id/${img.promo}")
            .placeholder(R.drawable.image_placeholder)
            .into(holder.binding.banner)
    }

    override fun getItemCount(): Int {
        return promo.size
    }

    inner class ViewHolder(val binding: PromoBinding): RecyclerView.ViewHolder(binding.root){

    }
}