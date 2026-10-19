package com.example.mpaz_neb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.mpaz_neb.navigation.AppNavigation
import com.example.mpaz_neb.ui.theme.MPAZNEBTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MPAZNEBTheme {
                AppNavigation()
            }
        }
    }
}