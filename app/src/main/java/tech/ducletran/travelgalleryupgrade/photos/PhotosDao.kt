package tech.ducletran.travelgalleryupgrade.photos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.OnConflictStrategy
import androidx.room.Delete
import androidx.room.Insert

@Dao
interface PhotosDao {

    @Query("SELECT * FROM photo")
    fun getAllPhotos(): LiveData<List<Photo>>

    @Query("SELECT * FROM photo WHERE id = :id")
    fun getPhotoById(id: Long): LiveData<Photo>

    @Query("DELETE FROM photo WHERE id = :photoId")
    suspend fun deletePhoto(photoId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photo: Photo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhotos(photos: List<Photo>)

    @Query("UPDATE photo SET isFavorite = :isFavorite WHERE id = :photoId")
    suspend fun setFavorite(photoId: Long, isFavorite: Boolean)

    @Query("UPDATE photo SET isFood = :isFood WHERE id = :photoId")
    suspend fun setFood(photoId: Long, isFood: Boolean)

    @Query("UPDATE photo SET isFriend = :isFriend WHERE id = :photoId")
    suspend fun setFriend(photoId: Long, isFriend: Boolean)
}