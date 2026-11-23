package com.example.mpaz_neb.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object MpazNebInstance {

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    val api: MpazNebApi by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")   // ← OJO: 10.0.2.2, NO localhost
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MpazNebApi::class.java)
    }
}