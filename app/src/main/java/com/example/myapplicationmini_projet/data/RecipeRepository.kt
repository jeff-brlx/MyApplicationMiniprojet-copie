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
            val response = api.getRecipes("Token 9c8b06d329136da358c2d00e76946b0111ce2c48", query, page)
            // Convertir les recettes reçues en entités pour Room
            val entities = response.results.map { recipe ->
                RecipeEntity(
                    pk = recipe.pk,
                    title = recipe.title,
                    featured_image = recipe.featured_image,
                    ingredients = recipe.ingredients  // recipe.ingredients est déjà de type List<String>

                )
            }
            // Sauvegarder dans la base locale
            database.recipeDao().insertRecipes(entities)
            // Retourner les recettes depuis l'API
            response.results
        } catch (e: Exception) {
            // En cas d'erreur, récupérer les recettes stockées dans le cache
            //val cachedRecipes = database.recipeDao().searchRecipes(query)
            val cachedRecipes = withContext(Dispatchers.IO) {
                database.recipeDao().searchRecipes(query)
            }
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