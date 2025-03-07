package com.example.myapplicationmini_projet.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecipeDao {

    @Query("SELECT * FROM recipes WHERE title LIKE '%' || :query || '%'")
    fun searchRecipes(query: String): List<RecipeEntity> // ✅ Retourne une liste

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipes(recipes: List<RecipeEntity>): List<Long> // ✅ Retourne les IDs insérés
}

