package com.example.hisar.data

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class Data(

	@field:SerializedName("data")
	val data: List<DataAgen>

): Parcelable {
	@Parcelize
	data class DataAgen(

		@field:SerializedName("role")
		val role: String? = null,

		@field:SerializedName("foto")
		val foto: String? = null,

		@field:SerializedName("nama")
		val nama: String? = null,

		@field:SerializedName("total_jamaah")
		val totalJamaah: Int? = null,

		@field:SerializedName("no_ktp")
		val noKtp: String? = null,

		@field:SerializedName("id")
		val id: String? = null,

		@field:SerializedName("dibuat_pada")
		val dibuatPada: String? = null,

		@field:SerializedName("alamat")
		val alamat: String? = null,

		@field:SerializedName("no_telepon")
		val noTelepon: String? = null,

		@field:SerializedName("username")
		val username: String? = null
	) : Parcelable

}

