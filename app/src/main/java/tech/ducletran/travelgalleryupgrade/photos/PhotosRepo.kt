package tech.ducletran.travelgalleryupgrade.photos

import androidx.lifecycle.LiveData
import tech.ducletran.travelgalleryupgrade.utils.DateUtils

class PhotosRepo(
    private val photosDao: PhotosDao
) {
    fun loadAllPhotos(): LiveData<List<Photo>> = photosDao.getAllPhotos()

    suspend fun addPhotos(photos: List<Photo>) {
        photosDao.insertPhotos(photos.map {
            if (it.dateTaken.isNotEmpty())
                it.dateTaken = DateUtils.convertDateBetweenFormats(
                    it.dateTaken,DateUtils.FORMAT_DATE_FROM_FILE, DateUtils.FORMAT_DATE_DETAILS)
            if (it.description == "cof")
                it.description = ""
            it
        })
    }

    suspend fun updatePhoto(photo: Photo) = photosDao.updatePhoto(photo)

    fun loadPhoto(photoId: Long) = photosDao.getPhotoById(photoId)

    fun loadFavoritePhotos() = photosDao.getFavoritePhotos()

    fun loadFriendPhotos() = photosDao.getFriendPhotos()

    fun loadFoodPhotos() = photosDao.getFoodPhotos()

    suspend fun removePhoto(photoId: Long) = photosDao.deletePhoto(photoId)

    suspend fun setPhotoFavorite(photoId: Long, isFavorite: Boolean) =
            photosDao.setFavorite(photoId, isFavorite)

    suspend fun setPhotoFood(photoId: Long, isFood: Boolean) =
            photosDao.setFood(photoId, isFood)

    suspend fun setPhotoFriend(photoId: Long, isFriend: Boolean) =
            photosDao.setFriend(photoId, isFriend)

    suspend fun getPhotoWithId(photoId: Long) = photosDao.getPhotoWithId(photoId)
}