package com.example.mpaz_neb.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = MpazColorPrincipal,
    onPrimary = MpazColorLetra,
    secondary = MpazColorAmarillo,
    onSecondary = MpazColorLetra,
    background = MpazColorFondoOscuro,
    onBackground = MpazColorTextoClaro,
    surface = MpazColorSuperficieOscura,
    onSurface = MpazColorTextoClaro,
    error = MpazColorLetra
)

private val LightColorScheme = lightColorScheme(
    primary = MpazColorPrincipal,
    onPrimary = MpazColorBlanco,
    secondary = MpazColorAmarillo,
    onSecondary = MpazColorLetra,
    background = MpazColorFondo,
    onBackground = MpazColorLetra,
    surface = MpazColorBlanco,
    onSurface = MpazColorLetra,
    surfaceVariant = MpazColorFondoSecundario,
    onSurfaceVariant = MpazColorTextoSecundario,
    outline = MpazColorBorde,
    error = MpazColorLetra
)

@Composable
fun MPAZNEBTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}