package com.example.myapplicationmini_projet.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.myapplicationmini_projet.viewmodel.RecipeViewModel
import com.example.myapplicationmini_projet.data.Recipe
import com.example.myapplicationmini_projet.utils.decodeHtml //  Import depuis Utils.kt

@Composable
fun RecipeListScreen(navController: NavController, viewModel: RecipeViewModel = viewModel()) {
    val recipes by viewModel.recipes.collectAsState()
    val listState = rememberLazyListState()

    // Détecte le scroll en fin de liste et déclenche le chargement de la page suivante
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { index ->
                if (index == recipes.lastIndex && recipes.isNotEmpty()) {
                    viewModel.loadNextPage()
                }
            }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar { viewModel.loadRecipes(it) }
        CategoryFilter { category ->
            viewModel.loadRecipes(if (category == "All") "" else category)
        }

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 4.dp)
        ) {
            items(recipes) { recipe ->
                RecipeCard(recipe) { navController.navigate("detail/${recipe.pk}") }
            }
            // Affiche un indicateur de chargement en bas de liste
            if (viewModel.isLoading.value) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}


@Composable
fun SearchBar(onSearch: (String) -> Unit) {
    var query by remember { mutableStateOf("") }

    OutlinedTextField(
        value = query,
        onValueChange = {
            query = it
            onSearch(it)
        },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Rechercher")
        },
        placeholder = { Text("Search", fontSize = 18.sp) },
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
}

@Composable
fun CategoryFilter(onCategorySelected: (String) -> Unit) {
    val categories = listOf("All", "Chicken", "Soup", "Dessert", "Vegetarian", "French")
    var selectedCategory by remember { mutableStateOf("All") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { category ->
            Button(
                onClick = {
                    selectedCategory = category
                    onCategorySelected(category)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedCategory == category) MaterialTheme.colorScheme.primary else Color.LightGray
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .height(40.dp)
            ) {
                Text(category, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun RecipeCard(recipe: Recipe, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column {
            Image(
                painter = rememberAsyncImagePainter(recipe.featured_image),
                contentDescription = recipe.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            )
            Text(
                text = decodeHtml(recipe.title), //  Nettoie les caractères HTML des titres
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}