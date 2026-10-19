package com.example.mpaz_neb.viewmodels

import androidx.lifecycle.ViewModel
import com.example.mpaz_neb.navigation.NavigationEvent
import com.example.mpaz_neb.navigation.Screen
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class MainViewModel : ViewModel() {

    private val _navigationEvents = MutableSharedFlow<NavigationEvent>()
    val navigationEvents: SharedFlow<NavigationEvent> = _navigationEvents.asSharedFlow()

    fun navigateTo(route: Screen) {
        _navigationEvents.tryEmit(NavigationEvent.NavigateTo(route))
    }
}