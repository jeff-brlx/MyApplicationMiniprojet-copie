package com.example.myapplicationmini_projet.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecipeDao {

    @Query("SELECT * FROM recipes WHERE title LIKE '%' || :query || '%'")
    fun searchRecipes(query: String): List<RecipeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipes(recipes: List<RecipeEntity>): List<Long>

    @Query("SELECT * FROM recipes WHERE pk = :recipeId LIMIT 1")
    fun getRecipeById(recipeId: Int): RecipeEntity?
}