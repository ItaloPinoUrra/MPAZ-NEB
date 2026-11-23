package com.example.mpaz_neb.data.remote


import com.example.mpaz_neb.data.model.Post
import retrofit2.http.GET

interface ApiService {
    @GET("posts")
    suspend fun getPosts(): List<Post>
}