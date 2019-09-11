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
    fun getPhotoById(id: Long): Single<Photo>

    @Delete
    fun deletePhoto(photo: Photo)

    @Delete
    fun deleteAllPhotos(photos: List<Photo>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhoto(photo: Photo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhotos(photos: List<Photo>): Completable

}