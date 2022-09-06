package com.example.klikin.modelo.domain

import com.example.klikin.modelo.api.Social

data class ShopInfo(
    val name: String,
    val description: String,
    val image: String,
    val longitude: String,
    val latitude: String,
    val social: Social,
    val icon: String,
    val phone: String,
    val email: String
)
