package com.example.hisar.data

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class ResRiwayatJamaah(
	val data: ArrayList<Riwayat>
): Parcelable {
	@Parcelize
	data class Riwayat(
		val jamaahId: String? = null,
		val id: Int? = null,
		val dibuat_pada: String,
		val value: String? = null
	) : Parcelable
}


