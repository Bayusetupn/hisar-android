package com.example.hisar.data

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class RiwayatLogin(

	@field:SerializedName("data")
	val data: ArrayList<Login>
): Parcelable{
	@Parcelize
	data class Login(

		@field:SerializedName("login")
		val login: String? = null,

		@field:SerializedName("userId")
		val userId: String? = null
	) : Parcelable
}


