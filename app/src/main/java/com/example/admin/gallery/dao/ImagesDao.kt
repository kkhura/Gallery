package com.example.admin.gallery.quotesHome.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.admin.gallery.model.ImageModel


@Dao
interface ImagesDao {

    @Insert
    fun insert(category: ImageModel)

    @Query("SELECT * FROM images")
    fun getAllData(): LiveData<List<ImageModel>>

    @Query("SELECT * FROM images WHERE tags LIKE '%'+:tag+'%'")
    fun getImages(tag: String): LiveData<List<ImageModel>>
}