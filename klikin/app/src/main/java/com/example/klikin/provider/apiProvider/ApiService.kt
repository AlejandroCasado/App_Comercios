package com.example.klikin.provider.apiProvider

import com.example.klikin.modelo.domain.ShopApi
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("public")
    suspend fun getInfoEmpress(): Response<List<ShopApi>>
}