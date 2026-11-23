package com.example.mpaz_neb.repository

import com.example.mpaz_neb.data.model.Post
import com.example.mpaz_neb.data.remote.RetrofitInstance

class PostRepository {
    suspend fun getPosts(): List<Post> {
        return RetrofitInstance.api.getPosts()
    }
}