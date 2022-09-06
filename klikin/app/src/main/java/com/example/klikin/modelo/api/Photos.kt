package com.example.klikin.modelo.api

data class Photos(
    val format: String,
    val _id: String,
    val thumbnails: Thumbnails,
    val url: String,
)
