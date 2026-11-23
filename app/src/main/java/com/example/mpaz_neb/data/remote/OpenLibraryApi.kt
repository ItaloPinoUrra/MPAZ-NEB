package com.example.mpaz_neb.data.remote

import com.example.mpaz_neb.data.model.OpenLibraryResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenLibraryApi {
    @GET("search.json")
    suspend fun getChildrenBooks(
        @Query("q") query: String = "subject:\"cuentos de hadas\" language:spa",
        @Query("limit") limit: Int = 20
    ): OpenLibraryResponse
}