package tech.ducletran.travelgalleryupgrade.photos

import androidx.lifecycle.LiveData
import io.reactivex.Completable
import io.reactivex.Flowable

class PhotosService(
    private val photosDao: PhotosDao
) {
    fun loadAllPhotos(): LiveData<List<Photo>> = photosDao.getAllPhotos()

    suspend fun addPhotos(photos: List<Photo>) {
        photosDao.insertPhotos(photos)
    }

    fun loadPhoto(photoId: Long): Flowable<Photo> = photosDao.getPhotoById(photoId)

    fun setPhotoFavorite(photoId: Long, isFavorite: Boolean): Completable =
            photosDao.setFavorite(photoId, isFavorite)

    fun setPhotoFood(photoId: Long, isFood: Boolean): Completable =
            photosDao.setFood(photoId, isFood)

    fun setPhotoFriend(photoId: Long, isFriend: Boolean): Completable =
            photosDao.setFriend(photoId, isFriend)
}