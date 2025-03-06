package com.example.myapplicationmini_projet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.myapplicationmini_projet.data.RecipeRepository
import com.example.myapplicationmini_projet.navigation.AppNavHost
import com.example.myapplicationmini_projet.ui.theme.MyApplicationMiniprojetTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialisation de la base de données dans le Repository
        RecipeRepository.init(this)
        setContent {
            MyApplicationMiniprojetTheme {
                val navController = rememberNavController()
                AppNavHost(navController)

            }
        }
    }
}