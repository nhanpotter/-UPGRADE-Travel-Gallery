package tech.ducletran.travelgalleryupgrade

import androidx.room.Database
import androidx.room.RoomDatabase
import tech.ducletran.travelgalleryupgrade.photos.Photo
import tech.ducletran.travelgalleryupgrade.photos.PhotosDao

@Database(entities = [Photo::class], version = 1)
abstract class BaseDatabase: RoomDatabase() {

    abstract fun photosDao(): PhotosDao
}