package com.example.admin.gallery.reprository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.admin.gallery.database.MyDatabase
import com.example.admin.gallery.model.ImageModel
import com.example.admin.gallery.quotesHome.dao.ImagesDao

class ImagesResprository {
    private var imagesDao: ImagesDao


    constructor(application: Application) {
        var myDatabase: MyDatabase = MyDatabase.getDatabase(application);
        imagesDao = myDatabase.getImagesDao()
    }

    fun getImages(): LiveData<List<ImageModel>> {
        return imagesDao.getAllData()

    }
}