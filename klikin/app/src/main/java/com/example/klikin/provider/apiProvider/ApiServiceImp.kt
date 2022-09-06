package com.example.klikin.provider.apiProvider

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiServiceImp{

    private val pathUrl = "https://prod.klikin.com/commerces/"

    private val api: ApiService

    init {
        api = getRetrofitApi()
    }

    private fun getRetrofitApi(): ApiService {
        val retrofit=  Retrofit.Builder()
            .baseUrl(pathUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ApiService::class.java)
    }

    fun getApi(): ApiService = api

}