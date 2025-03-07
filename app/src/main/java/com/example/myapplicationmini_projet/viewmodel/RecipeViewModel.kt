package com.example.myapplicationmini_projet.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.myapplicationmini_projet.data.RecipeRepository
import com.example.myapplicationmini_projet.data.Recipe

class RecipeViewModel : ViewModel() {
    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes

    // Indicateur de chargement pour éviter de lancer plusieurs appels simultanés
    val isLoading = mutableStateOf(false)

    private var currentPage = 1
    private var currentQuery = ""

    init {
        loadRecipes("")
    }

    fun loadRecipes(query: String, resetPage: Boolean = true) {
        viewModelScope.launch {
            if (resetPage) {
                currentPage = 1
                currentQuery = query
                _recipes.value = emptyList()
            }
            // Démarrer le chargement
            isLoading.value = true
            val newRecipes = RecipeRepository.fetchRecipes(query, currentPage)
            println("🎨 UI Loading ${newRecipes.size} recipes")
            _recipes.value = if (resetPage) newRecipes else _recipes.value + newRecipes
            isLoading.value = false
        }
    }

    fun loadNextPage() {
        if (!isLoading.value) { // Pour éviter plusieurs chargements simultanés
            currentPage++
            loadRecipes(currentQuery, resetPage = false)
        }
    }
}
