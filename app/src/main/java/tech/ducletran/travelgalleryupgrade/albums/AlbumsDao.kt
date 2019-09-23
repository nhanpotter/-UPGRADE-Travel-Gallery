package tech.ducletran.travelgalleryupgrade.albums

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import tech.ducletran.travelgalleryupgrade.photos.Photo

@Dao
interface AlbumsDao {

    @Query("SELECT * FROM photo INNER JOIN albumandphoto ON photo.id = photoId WHERE albumId = :albumId")
    fun getPhotosFromAlbum(albumId: Long): LiveData<List<Photo>>

    @Insert
    suspend fun addPhotosToAlbum(photos: List<AlbumAndPhoto>)

    @Query("DELETE FROM albumandphoto WHERE photoId = :photoId AND albumId = :albumId")
    suspend fun removePhotoFromAlbum(photoId: Long, albumId: Long)

    @Insert
    suspend fun addAlbum(album: Album)

    @Update
    suspend fun updateAlbum(album: Album)

    @Query("DELETE FROM album WHERE id = :albumId")
    suspend fun removeAlbum(albumId: Long)

    @Query("DELETE FROM albumandphoto WHERE albumId = :albumId")
    suspend fun removeAlbumAndPhoto(albumId: Long)

    @Query("SELECT * FROM album")
    fun getAllAlbums(): LiveData<List<Album>>

    @Query("SELECT * FROM album WHERE id = :albumId")
    fun getAlbumById(albumId: Long): LiveData<Album>
}