package com.example.myapplicationmini_projet.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey
    @ColumnInfo(name = "pk")
    val pk: Int,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "featured_image")
    val featured_image: String,

    @ColumnInfo(name = "ingredients")
    val ingredients: List<String>  // Type modifié pour correspondre au convertisseur
)