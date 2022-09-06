package com.example.klikin.modelo.domain

import com.example.klikin.modelo.api.*
import com.google.gson.annotations.SerializedName

data class ShopApi(
    val name: String,
    val address: Address,
    val social: Social,
    val contact: Contact,
    val category: String?,
    val latitude: String,
    val longitude: String,
    val shortDescription: String,
    val description: String,
    @SerializedName("openingHours")val timetable: String,
    @SerializedName("config")val timeZone: Config,
    @SerializedName("features")val featureSkill: List<String>,
    val ipadPhotos: IpadPhotos,
    val photos: List<Photos>,
    val logo: Logo,
    var distancia:Float? = null

    )
