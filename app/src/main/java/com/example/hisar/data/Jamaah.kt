package com.example.hisar.data

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class Jamaah(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null
): Parcelable {
	@Parcelize
	data class DataItem(

		@field:SerializedName("ktp")
		val ktp: String? = null,

		@field:SerializedName("berangkat")
		val berangkat: String? = null,

		@field:SerializedName("dp")
		val dp: Boolean? = null,

		@field:SerializedName("dibuat_pada")
		val dibuatPada: String? = null,

		@field:SerializedName("userId")
		val userId: String? = null,

		@field:SerializedName("alamat")
		val alamat: String? = null,

		@field:SerializedName("foto")
		val foto: String? = null,

		@field:SerializedName("nama")
		val nama: String? = null,

		@field:SerializedName("id")
		val id: String? = null,

		@field:SerializedName("daftarkan")
		val daftarkan: String? = null,

		@field:SerializedName("jenis_kelamin")
		val jenisKelamin: String? = null,

		@field:SerializedName("paket")
		val paket: String? = null,

		@field:SerializedName("no_telepon")
		val noTelepon: String? = null
	) : Parcelable
}


