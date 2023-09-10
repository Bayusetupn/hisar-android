package com.example.hisar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.hisar.data.Banner
import com.example.hisar.databinding.ImageSliderBinding

class SlideAdapter(private val img : ArrayList<Banner.Item>,val viewPager2: ViewPager2):
    RecyclerView.Adapter<SlideAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideAdapter.ViewHolder {
        val binding = ImageSliderBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SlideAdapter.ViewHolder, position: Int) {
        val image = img[position]
        Glide.with(holder.itemView.context)
            .load("https://api.hisar.my.id/${image.promo}")
            .placeholder(R.drawable.image_placeholder)
            .into(holder.binding.banner)
        if (position == img.size-1){
            viewPager2.post(runnable)
        }
    }

    private val runnable = Runnable {
        img.addAll(img)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return img.size
    }

    inner class ViewHolder(val binding:ImageSliderBinding):RecyclerView.ViewHolder(binding.root){
    }

}