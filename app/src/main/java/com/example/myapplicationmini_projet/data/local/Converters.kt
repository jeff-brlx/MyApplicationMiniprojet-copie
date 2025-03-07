package com.example.myapplicationmini_projet.data.local


import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromIngredientsList(value: List<String>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toIngredientsList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }
}

