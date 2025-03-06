package com.example.myapplicationmini_projet.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplicationmini_projet.ui.screens.SplashScreen
import com.example.myapplicationmini_projet.ui.screens.RecipeListScreen
import com.example.myapplicationmini_projet.ui.screens.RecipeDetailScreen

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = "splash") {

        composable(route = "splash") {
            SplashScreen {
                navController.navigate("recipe_list") {
                    popUpTo("splash") { inclusive = true } // Supprime l'écran Splash de la pile
                }
            }
        }

        composable(route = "recipe_list") {
            RecipeListScreen(navController)
        }

        composable(route = "detail/{recipeId}") { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId") ?: "0"
            RecipeDetailScreen(recipeId, navController)
        }
    }
}