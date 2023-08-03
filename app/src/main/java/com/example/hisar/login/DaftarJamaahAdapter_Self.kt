package com.example.hisar.login

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hisar.agen.ProfileJamaah_Agen
import com.example.hisar.agen.ProfileJamaah_Self
import com.example.hisar.data.Jamaah
import com.example.hisar.databinding.ListDaftarAgenBinding

class DaftarJamaahAdapter_Self(private var data: ArrayList<Jamaah.DataItem>, private val legth:Int):
RecyclerView.Adapter<DaftarJamaahAdapter_Self.ViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DaftarJamaahAdapter_Self.ViewHolder {
        val binding = ListDaftarAgenBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    fun filteredList(data: ArrayList<Jamaah.DataItem>){
        this.data = data
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: DaftarJamaahAdapter_Self.ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
        holder.open.setOnClickListener {
            val intent = Intent(holder.itemView.context,ProfileJamaah_Self::class.java)
                .putExtra("id",item.id)
                .putExtra("userId",item.userId)
                .putExtra("nama",item.nama)
                .putExtra("alamat",item.alamat)
                .putExtra("ktp",item.ktp)
                .putExtra("kelamin",item.jenisKelamin)
                .putExtra("telp",item.noTelepon)
                .putExtra("bergabung",item.dibuatPada)
                .putExtra("berangkat",item.berangkat)
                .putExtra("keterangan",item.dp)
                .putExtra("paket",item.paket)
                .putExtra("didaftarkan",item.daftarkan)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return if (data.size < 1){
            0
        }else if(data.size == 1){
            1
        }else if(data.size == 2){
            2
        }else{
            legth
        }
    }

    inner class ViewHolder(private val binding: ListDaftarAgenBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: Jamaah.DataItem){
            binding.nama.text = item.nama
            binding.telp.text = item.noTelepon
        }
        val open = binding.open
    }

}