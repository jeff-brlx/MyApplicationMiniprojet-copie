package com.example.myapplicationmini_projet.viewmodel

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
            }

            val newRecipes = RecipeRepository.fetchRecipes(query)
            _recipes.value = if (resetPage) newRecipes else _recipes.value + newRecipes
        }
    }

    fun loadNextPage() {
        currentPage++
        loadRecipes(currentQuery, resetPage = false)
    }
}