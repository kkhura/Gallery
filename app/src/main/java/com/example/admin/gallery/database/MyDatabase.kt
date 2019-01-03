package com.example.admin.gallery.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.admin.gallery.model.ImageModel
import com.example.admin.gallery.quotesHome.dao.ImagesDao


@Database(entities = [(ImageModel::class)], version = 1)
abstract class MyDatabase : RoomDatabase() {

    abstract fun getImagesDao(): ImagesDao

    companion object {
        @Volatile
        private var INSTANCE: MyDatabase? = null

        fun getDatabase(context: Context): MyDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        MyDatabase::class.java,
                        "imageDatabase"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}