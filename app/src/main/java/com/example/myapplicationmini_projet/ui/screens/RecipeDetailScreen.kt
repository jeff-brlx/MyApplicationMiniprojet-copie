package com.example.myapplicationmini_projet.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.myapplicationmini_projet.data.Recipe
import com.example.myapplicationmini_projet.data.RecipeRepository
import kotlinx.coroutines.launch
import com.example.myapplicationmini_projet.utils.decodeHtml //  Import depuis Utils.kt

@Composable
fun RecipeDetailScreen(recipeId: String, navController: NavController) {
    val recipe = remember { mutableStateOf<Recipe?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(recipeId) {
        coroutineScope.launch {
            if (recipeId.isNotEmpty() && recipeId != "0") {
                println("Fetching recipe with ID: $recipeId") // Debugging
                recipe.value = RecipeRepository.fetchRecipeById(recipeId)
            } else {
                println("Error: Invalid recipeId, skipping API call") // Debugging
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (recipe.value != null) {
            val currentRecipe = recipe.value!!

            Image(
                painter = rememberAsyncImagePainter(currentRecipe.featured_image),
                contentDescription = currentRecipe.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            //  Nettoyage du titre avec decodeHtml()
            Text(
                text = decodeHtml(currentRecipe.title),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text("Ingrédients :", fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(4.dp))

            currentRecipe.ingredients.forEach { ingredient ->
                Text(text = "- $ingredient")
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        // Bouton retour
        Button(
            onClick = { navController.navigateUp() },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
        ) {
            Text("Retour")
        }
    }
}