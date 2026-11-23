package com.example.mpaz_neb.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mpaz_neb.data.model.GoogleBookItem
import com.example.mpaz_neb.repository.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BookViewModel : ViewModel() {
    private val repository = BookRepository()

    private val _bookList = MutableStateFlow<List<GoogleBookItem>>(emptyList())
    val bookList: StateFlow<List<GoogleBookItem>> = _bookList

    private val _cargando = MutableStateFlow(true)
    val cargando: StateFlow<Boolean> = _cargando

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        fetchBooks()
    }

    private fun fetchBooks() {
        viewModelScope.launch {
            try {
                _cargando.value = true
                _error.value = null
                _bookList.value = repository.getChildrenBooks()
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Error desconocido"
            } finally {
                _cargando.value = false
            }
        }
    }
}