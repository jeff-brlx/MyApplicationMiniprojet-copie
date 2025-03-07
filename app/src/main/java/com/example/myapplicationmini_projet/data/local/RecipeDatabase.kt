package com.example.myapplicationmini_projet.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * Définition de la base de données Room pour les recettes.
 */
@Database(
    entities = [RecipeEntity::class],  // La ou les entités que tu déclares
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)     // Les convertisseurs pour listes, dates, etc.
abstract class RecipeDatabase : RoomDatabase() {

    // Déclare le DAO associé
    abstract fun recipeDao(): RecipeDao

    companion object {
        @Volatile
        private var INSTANCE: RecipeDatabase? = null

        fun getDatabase(context: Context): RecipeDatabase {
            // Vérifie si l'instance existe déjà
            return INSTANCE ?: synchronized(this) {
                // Sinon, la construit et l'assigne à INSTANCE
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecipeDatabase::class.java,
                    "recipe_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
