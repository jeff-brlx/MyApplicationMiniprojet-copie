package com.example.myapplicationmini_projet.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "recipes")
@TypeConverters(Converters::class)
data class RecipeEntity(
    @PrimaryKey
    @ColumnInfo(name = "pk")
    val pk: Int,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "featured_image")
    val featured_image: String,

    @ColumnInfo(name = "ingredients")
    val ingredients: List<String>
)
