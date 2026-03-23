package com.jesus.fisioapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    //Para conectar con el PC
    private const val BASE_URL = "http://10.0.2.2:8080"
//    private const val BASE_URL = "http://192.168.0.13:8080"

    val apiService: FisioApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) //traduce los JSON
            .build()
            .create(FisioApiService::class.java)
    }
}