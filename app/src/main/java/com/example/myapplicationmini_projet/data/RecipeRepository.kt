package com.example.myapplicationmini_projet.data

import android.content.Context
import com.example.myapplicationmini_projet.data.local.RecipeDatabase
import com.example.myapplicationmini_projet.data.local.RecipeEntity
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RecipeRepository {
    private lateinit var database: RecipeDatabase
    fun init(context: Context) {
        database = RecipeDatabase.getDatabase(context)
    }

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
            println(" Fetching recipes from API: query='$query', page=$page")
            val response = api.getRecipes("Token 9c8b06d329136da358c2d00e76946b0111ce2c48", query, page)

            println("API Response: total ${response.count} recipes found")
            println("Received ${response.results.size} recipes")

            if (response.results.isEmpty()) {
                println("API returned empty list!")
            }

            val entities = response.results.map { recipe ->
                RecipeEntity(
                    pk = recipe.pk,
                    title = recipe.title,
                    featured_image = recipe.featured_image,
                    ingredients = recipe.ingredients
                )
            }

            // Effectuer l'insertion en arrière-plan
            withContext(Dispatchers.IO) {
                database.recipeDao().insertRecipes(entities)
                println("💾 Saved ${entities.size} recipes in Room")
            }

            response.results
        } catch (e: Exception) {
            println(" API Error: ${e.message}")

            withContext(Dispatchers.IO) {
                val cachedRecipes = database.recipeDao().searchRecipes(query)
                println("💾 Loading ${cachedRecipes.size} cached recipes from Room")
                cachedRecipes.map { entity ->
                    Recipe(
                        pk = entity.pk,
                        title = entity.title,
                        featured_image = entity.featured_image,
                        ingredients = entity.ingredients
                    )
                }
            }
        }
    }


    suspend fun fetchRecipeById(recipeId: String): Recipe? {
        return try {
            println("Fetching recipe with ID: $recipeId from API") // Debugging
            val recipe = api.getRecipeById("Token 9c8b06d329136da358c2d00e76946b0111ce2c48", recipeId)

            // Sauvegarder la recette dans la base locale
            withContext(Dispatchers.IO) {
                database.recipeDao().insertRecipes(
                    listOf(
                        RecipeEntity(
                            pk = recipe.pk,
                            title = recipe.title,
                            featured_image = recipe.featured_image,
                            ingredients = recipe.ingredients
                        )
                    )
                )
            }

            recipe
        } catch (e: Exception) {
            println(" API Error: ${e.message}, fetching from local database")

            // Charger la recette depuis Room en mode hors-ligne
            withContext(Dispatchers.IO) {
                val cachedRecipe = database.recipeDao().getRecipeById(recipeId.toInt())
                cachedRecipe?.let {
                    Recipe(
                        pk = it.pk,
                        title = it.title,
                        featured_image = it.featured_image,
                        ingredients = it.ingredients
                    )
                }
            }
        }
    }

}