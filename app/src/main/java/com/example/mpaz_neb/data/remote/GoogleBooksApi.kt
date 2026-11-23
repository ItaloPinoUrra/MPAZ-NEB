package com.example.mpaz_neb.data.remote

import com.example.mpaz_neb.data.model.GoogleBooksResponse
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.mpaz_neb.BuildConfig

interface GoogleBooksApi {
    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String = "cuentos infantiles",
        @Query("langRestrict") lang: String = "es",
        @Query("maxResults") maxResults: Int = 20,
        @Query("key") apiKey: String = BuildConfig.GOOGLE_BOOKS_API_KEY
    ): GoogleBooksResponse
}