package com.example.hisar

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hisar.admin.ProfileAgen
import com.example.hisar.agen.AgenProfile_Agen
import com.example.hisar.data.Data
import com.example.hisar.databinding.ListRangkingBinding

class DaftarRank_Agen(private var data: ArrayList<Data.DataAgen>, private val length:Int):RecyclerView.Adapter<DaftarRank_Agen.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DaftarRank_Agen.ViewHolder {
        val binding = ListRangkingBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    fun sortData(newData: ArrayList<Data.DataAgen>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: DaftarRank_Agen.ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
        when(position){
            0 -> holder.diamond.visibility = View.VISIBLE
            1 -> holder.platinum.visibility = View.VISIBLE
            2 -> holder.gold.visibility = View.VISIBLE
        }
        holder.rank.text = "${position + 1}"
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
        return data.size
    }

    inner class ViewHolder(private val binding: ListRangkingBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item:Data.DataAgen){
            binding.nama.text = item.nama
            binding.telp.text = item.noTelepon
        }
        val diamond = binding.diamond
        val platinum = binding.platinum
        val gold = binding.gold
        val rank = binding.rank
        val open = binding.open
    }

}