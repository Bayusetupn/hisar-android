package com.example.hisar.data

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class DocJamaah(

	@field:SerializedName("data")
	val data: Doc
) : Parcelable

@Parcelize
data class Doc(

	@field:SerializedName("kk")
	val kk: String? = null,

	@field:SerializedName("passport")
	val passport: String? = null,

	@field:SerializedName("foto")
	val foto: String? = null,

	@field:SerializedName("ktp")
	val ktp: String? = null,

	@field:SerializedName("akteK")
	val akteK: String? = null,

	@field:SerializedName("akteN")
	val akteN: String? = null,

) : Parcelable
