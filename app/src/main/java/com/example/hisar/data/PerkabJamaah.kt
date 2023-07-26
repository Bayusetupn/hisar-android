package com.example.hisar.data

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class PerkabJamaah(
	val data: Perkab
) : Parcelable

@Parcelize
data class Perkab(
	val jamaahId: String? = null,
	val bookletSholawat: Boolean?,
	val bukuDoa: Boolean?,
	val kainIhram: Boolean?,
	val bukuPanduan: Boolean?,
	val mukena: Boolean?,
	val syal: Boolean?,
	val id: Int? = null,
	val kainBatik: Boolean?,
	val koperDll: Boolean?,
	val bookletPeta: Boolean?
) : Parcelable
