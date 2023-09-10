package com.example.hisar.data

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class Banner(

	@field:SerializedName("data")
	val data: ArrayList<Item>


) : Parcelable {
	@Parcelize
	data class Item(

		@field:SerializedName("promo")
		val promo: String? = null,

		@field:SerializedName("id")
		val id: String? = null
	) : Parcelable
}


