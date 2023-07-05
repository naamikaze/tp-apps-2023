package com.example.logo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Database(entities = [ProductLocal::class], version = 2 )
abstract class AppDatabase : RoomDatabase() {

    abstract fun productsDAO() : ProductsDAO

    companion object {
        @Volatile // Grantiza lectura o escritura desde multiples hilos de ejec
        private var _instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase = _instance ?: synchronized(this){
            _instance ?: buildDataBase(context)
        }

        private fun buildDataBase(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
                .fallbackToDestructiveMigration()
                .build()

        suspend fun clean (context: Context) = coroutineScope {
            launch(Dispatchers.IO){
                getInstance(context).clearAllTables()
            }
        }
    }

}