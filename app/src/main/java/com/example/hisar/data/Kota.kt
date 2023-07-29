package com.example.hisar.data

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class Kota(

	@field:SerializedName("kota_kabupaten")
	val kotaKabupaten: List<Kabupaten>
):Parcelable {
	@Parcelize
	data class Kabupaten(

		@field:SerializedName("nama")
		val nama: String? = null,

		@field:SerializedName("id")
		val id: Int? = null,

		@field:SerializedName("id_provinsi")
		val idProvinsi: String? = null
	) : Parcelable
}


