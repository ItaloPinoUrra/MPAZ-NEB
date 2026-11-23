package com.example.mpaz_neb.repository

import com.example.mpaz_neb.data.model.GoogleBookItem
import com.example.mpaz_neb.data.remote.GoogleBooksInstance

class BookRepository {
    suspend fun getChildrenBooks(): List<GoogleBookItem> {
        return GoogleBooksInstance.api.searchBooks().items ?: emptyList()
    }
}