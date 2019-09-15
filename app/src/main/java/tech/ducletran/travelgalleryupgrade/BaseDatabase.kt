package tech.ducletran.travelgalleryupgrade

import androidx.room.Database
import androidx.room.RoomDatabase
import tech.ducletran.travelgalleryupgrade.albums.Album
import tech.ducletran.travelgalleryupgrade.albums.AlbumAndPhoto
import tech.ducletran.travelgalleryupgrade.albums.AlbumsDao
import tech.ducletran.travelgalleryupgrade.photos.Photo
import tech.ducletran.travelgalleryupgrade.photos.PhotosDao

@Database(entities = [Photo::class, Album::class, AlbumAndPhoto::class], version = 2)
abstract class BaseDatabase : RoomDatabase() {

    abstract fun photosDao(): PhotosDao

    abstract fun albumsDao(): AlbumsDao
}