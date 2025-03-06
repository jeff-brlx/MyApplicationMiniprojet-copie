package com.example.myapplicationmini_projet.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RecipeRepository {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://food2fork.ca/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    private val api: RecipeApiService by lazy {
        retrofit.create(RecipeApiService::class.java)
    }

    suspend fun fetchRecipes(query: String, page: Int = 1): List<Recipe> {
        return try {
            val response = api.getRecipes("Token 9c8b06d329136da358c2d00e76946b0111ce2c48", query, page)
            response.results
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun fetchRecipeById(recipeId: String): Recipe? {
        return try {
            println("Fetching recipe with ID: $recipeId") // Debugging
            api.getRecipeById("Token 9c8b06d329136da358c2d00e76946b0111ce2c48", recipeId)
        } catch (e: Exception) {
            println("Error fetching recipe: ${e.message}") // Debugging
            null
        }
    }
}