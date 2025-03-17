package com.example.myapplicationmini_projet.ui.theme

import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Définition des couleurs
val OrangePrimary = Color(0xFFFF9800) //  Orange
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

//  Palette de couleurs claires
private val LightColorScheme = lightColorScheme(
    primary = OrangePrimary, // Définit l'orange pour les boutons
    onPrimary = Color.White, // Texte blanc pour le contraste
    secondary = PurpleGrey40,
    onSecondary = Color.White,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black
)

// Palette de couleurs sombres (optionnel)
private val DarkColorScheme = darkColorScheme(
    primary = OrangePrimary,
    onPrimary = Color.White,
    secondary = PurpleGrey80,
    onSecondary = Color.White,
    background = Color.Black,
    onBackground = Color.White,
    surface = Color.Black,
    onSurface = Color.White
)

// Appliquer le thème avec la nouvelle couleur orange
@Composable
fun MyApplicationMiniprojetTheme(
    darkTheme: Boolean = false, // Détermine si le mode sombre est activé
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}