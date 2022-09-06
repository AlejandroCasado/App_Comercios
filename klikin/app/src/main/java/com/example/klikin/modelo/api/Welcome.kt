package com.example.klikin.modelo.api

import com.example.klikin.modelo.api.Thumbnails

data class Welcome(
    val format: String,
    val thumbnails: Thumbnails,
    val url: String
)
