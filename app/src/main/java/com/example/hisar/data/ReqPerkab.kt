package com.example.hisar.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReqPerkab(

    @field:SerializedName("booklet_sholawat")
    val bookletSholawat: Boolean,

    @field:SerializedName("buku_doa")
    val bukuDoa: Boolean,

    @field:SerializedName("kain_ihram")
    val kainIhram: Boolean,

    @field:SerializedName("buku_panduan")
    val bukuPanduan: Boolean,

    @field:SerializedName("mukena")
    val mukena: Boolean,

    @field:SerializedName("syal")
    val syal: Boolean,

    @field:SerializedName("kain_batik")
    val kainBatik: Boolean,

    @field:SerializedName("koper_dll")
    val koperDll: Boolean,

    @field:SerializedName("booklet_peta")
    val bookletPeta: Boolean
) : Parcelable

