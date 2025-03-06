package com.example.myapplicationmini_projet.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")

data class RecipeEntity(
    @PrimaryKey val pk: Int,
    val title: String,
    val featured_image: String,
    // On stockera les ingrédients sous forme de chaîne JSON
    val ingredients: String
)
