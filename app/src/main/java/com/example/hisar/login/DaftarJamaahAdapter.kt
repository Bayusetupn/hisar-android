package com.example.hisar.login

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.hisar.data.Data
import com.example.hisar.data.Jamaah
import com.example.hisar.databinding.ListDaftarAgenBinding

class DaftarJamaahAdapter(private var data: ArrayList<Jamaah.DataItem>,val legth:Int):
RecyclerView.Adapter<DaftarJamaahAdapter.ViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DaftarJamaahAdapter.ViewHolder {
        val binding = ListDaftarAgenBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    fun filteredList(data: ArrayList<Jamaah.DataItem>){
        this.data = data
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: DaftarJamaahAdapter.ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        if (data.size < 1){
            return 0
        }else if(data.size == 1){
            return 1
        }else if(data.size == 2){
            return 2
        }else{
            return legth
        }
    }

    inner class ViewHolder(private val binding: ListDaftarAgenBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: Jamaah.DataItem){
            binding.nama.text = item.nama
            binding.telp.text = item.noTelepon
        }
    }

}