package com.example.hisar

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hisar.data.RiwayatLogin
import com.example.hisar.databinding.RiwayatLoginBinding
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class RiwayatLoginAdapter(private val data: ArrayList<RiwayatLogin.Login>): RecyclerView.Adapter<RiwayatLoginAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RiwayatLoginAdapter.ViewHolder {
        val binding = RiwayatLoginBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RiwayatLoginAdapter.ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return if (data.size <= 3){
            data.size
        }else{
            3
        }
    }

    inner class ViewHolder(private val binding: RiwayatLoginBinding) : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(item: RiwayatLogin.Login){
            val date = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val time = DateTimeFormatter.ofPattern("HH:mm")
            binding.date.text = Instant.parse(item.login).atZone(ZoneId.of("Asia/Jakarta")).format(date)
            binding.time.text = "${Instant.parse(item.login).atZone(ZoneId.of("Asia/Jakarta")).format(time)} WIB"
        }
    }
}