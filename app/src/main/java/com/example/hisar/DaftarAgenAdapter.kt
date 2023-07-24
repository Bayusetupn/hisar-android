package com.example.hisar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hisar.data.Data
import com.example.hisar.databinding.ListDaftarAgenBinding

class DaftarAgenAdapter(private val data: List<Data.DataAgen>,private val length:Int):RecyclerView.Adapter<DaftarAgenAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DaftarAgenAdapter.ViewHolder {
        val binding = ListDaftarAgenBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DaftarAgenAdapter.ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        if (data.size <= 1){
            return 1
        }else{
            return length
        }
    }

    inner class ViewHolder(private val binding: ListDaftarAgenBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item:Data.DataAgen){
            binding.nama.text = item.nama
            binding.telp.text = item.noTelepon
        }
    }

}