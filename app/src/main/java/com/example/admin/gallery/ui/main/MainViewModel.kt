package com.example.admin.gallery.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.admin.gallery.model.ImageModel
import com.example.admin.gallery.reprository.ImagesResprository

class MainViewModel : AndroidViewModel {
    private var imagesResprository: ImagesResprository
    private var imagesList: LiveData<List<ImageModel>>

    constructor(application: Application) : super(application) {
        imagesResprository = ImagesResprository(application)
        imagesList = imagesResprository.getImages()

    }

    fun getImagesList(): LiveData<List<ImageModel>> {
        return imagesList
    }
}