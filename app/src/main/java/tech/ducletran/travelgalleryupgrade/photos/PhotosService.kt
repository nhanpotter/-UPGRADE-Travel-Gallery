package tech.ducletran.travelgalleryupgrade.photos

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class PhotosService(
    private val photosDao: PhotosDao
) {
    fun loadAllPhotos(): Flowable<List<Photo>> {

        return photosDao.getAllPhotos()
    }

    fun addPhotos(photos: List<Photo>): Completable
            = photosDao.insertPhotos(photos)
}