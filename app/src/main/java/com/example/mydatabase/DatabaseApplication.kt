package com.example.mydatabase

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.mydatabase.room.AppDatabase

class DatabaseApplication : Application() {
    private var _appDatabase: AppDatabase? = null
    val appDatabase get() = requireNotNull(_appDatabase)

    override fun onCreate() {
        super.onCreate()
        _appDatabase = Room
            .databaseBuilder(
                this,
                AppDatabase::class.java,
                "database"
            )
            .allowMainThreadQueries()
            .build()
    }
}

val Context.appDatabase: AppDatabase
    get() = when (this) {
        is DatabaseApplication -> appDatabase
        else -> applicationContext.appDatabase
    }