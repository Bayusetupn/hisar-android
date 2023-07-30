package com.example.hisar

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hisar.data.ResRiwayatJamaah
import com.example.hisar.databinding.RiwayatLoginBinding
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DaftarRiwayatAdapter(val data: ArrayList<ResRiwayatJamaah.Riwayat>):
    RecyclerView.Adapter<DaftarRiwayatAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DaftarRiwayatAdapter.ViewHolder {
        val binding = RiwayatLoginBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DaftarRiwayatAdapter.ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        data.size.apply {
            if (data.size <= 3){
                return data.size
            }else{
                return 3
            }
        }
    }

    inner class ViewHolder(val binding: RiwayatLoginBinding): RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(item: ResRiwayatJamaah.Riwayat){
            binding.date.text = item.value
            binding.time.text = "Diubah pada : ${item.dibuat_pada}"
        }
    }
}