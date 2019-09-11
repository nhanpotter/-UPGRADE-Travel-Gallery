package tech.ducletran.travelgalleryupgrade.photos

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface PhotosDao {

    @Query("SELECT * FROM photo")
    fun getAllPhotos(): Flowable<List<Photo>>

    @Query("SELECT * FROM photo WHERE id = :id")
    fun getPhotoById(id: Long): Flowable<Photo>

    @Delete
    fun deletePhoto(photo: Photo)

    @Delete
    fun deleteAllPhotos(photos: List<Photo>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhoto(photo: Photo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhotos(photos: List<Photo>): Completable

    @Query("UPDATE photo SET isFavorite = :isFavorite WHERE id = :photoId")
    fun setFavorite(photoId: Long, isFavorite: Boolean): Completable

    @Query("UPDATE photo SET isFood = :isFood WHERE id = :photoId")
    fun setFood(photoId: Long, isFood: Boolean): Completable

    @Query("UPDATE photo SET isFriend = :isFriend WHERE id = :photoId")
    fun setFriend(photoId: Long, isFriend: Boolean): Completable

}