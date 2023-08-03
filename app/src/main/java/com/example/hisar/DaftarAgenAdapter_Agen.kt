package com.example.hisar

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hisar.admin.ProfileAgen
import com.example.hisar.agen.AgenActivity
import com.example.hisar.agen.AgenProfile_Agen
import com.example.hisar.data.Data
import com.example.hisar.databinding.ListDaftarAgenBinding

class DaftarAgenAdapter_Agen(private var data: ArrayList<Data.DataAgen>, private val length:Int):RecyclerView.Adapter<DaftarAgenAdapter_Agen.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DaftarAgenAdapter_Agen.ViewHolder {
        val binding = ListDaftarAgenBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    fun filteredList(data: ArrayList<Data.DataAgen>){
        this.data = data
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: DaftarAgenAdapter_Agen.ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
        holder.open.setOnClickListener {
            val intent = Intent(holder.itemView.context,AgenProfile_Agen::class.java)
                .putExtra("nama",item.nama)
                .putExtra("username",item.username)
                .putExtra("alamat",item.alamat)
                .putExtra("bergabung",item.dibuatPada)
                .putExtra("telp",item.noTelepon)
                .putExtra("totalJ",item.totalJamaah.toString())
                .putExtra("foto",item.foto)
                .putExtra("id",item.id)
                .putExtra("role",item.role)
                .putExtra("ktp",item.noKtp)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        if (data.size <= 1){
            return 1
        }else if(data.size == 2){
            return 2
        }else {
            return length
        }
    }

    inner class ViewHolder(private val binding: ListDaftarAgenBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item:Data.DataAgen){
            binding.nama.text = item.nama
            binding.telp.text = item.noTelepon
        }

        val open = binding.open
    }

}