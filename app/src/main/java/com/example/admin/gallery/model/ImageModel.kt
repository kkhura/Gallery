package com.example.admin.gallery.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class ImageModel(@PrimaryKey @ColumnInfo(name = "name") var _id: Int,
                               @ColumnInfo(name = "userImageURL") var userImageURL : String, @ColumnInfo(name = "tags") var tags: String)
