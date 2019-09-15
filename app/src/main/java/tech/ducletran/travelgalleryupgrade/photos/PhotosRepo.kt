package tech.ducletran.travelgalleryupgrade.photos

import androidx.lifecycle.LiveData

class PhotosRepo(
    private val photosDao: PhotosDao
) {
    fun loadAllPhotos(): LiveData<List<Photo>> = photosDao.getAllPhotos()

    suspend fun addPhotos(photos: List<Photo>) {
        photosDao.insertPhotos(photos)
    }

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
}