package com.example.myapplicationmini_projet.data

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RecipeApiService {
    @GET("recipe/search/")
    suspend fun getRecipes(
        @Header("Authorization") authToken: String = "Token 9c8b06d329136da358c2d00e76946b0111ce2c48",
        @Query("query") searchQuery: String,
        @Query("page") page: Int = 1
    ): RecipeResponse

    @GET("recipe/get/")
    suspend fun getRecipeById(
        @Header("Authorization") authToken: String = "Token 9c8b06d329136da358c2d00e76946b0111ce2c48",
        @Query("id") recipeId: String
    ): Recipe
}

data class RecipeResponse(val count: Int, val results: List<Recipe>)

data class Recipe(
    val pk: Int,
    val title: String,
    val featured_image: String,
    val ingredients: List<String>
)