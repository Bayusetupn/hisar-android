package com.example.hisar.data

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class Provinsi(

	@field:SerializedName("provinsi")
	val provinsi: List<ProvinsiItem>
): Parcelable {
	@Parcelize
	data class ProvinsiItem(

		@field:SerializedName("nama")
		val nama: String? = null,

		@field:SerializedName("id")
		val id: Int? = null
	) : Parcelable
}


