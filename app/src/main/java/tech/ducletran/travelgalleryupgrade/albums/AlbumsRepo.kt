package tech.ducletran.travelgalleryupgrade.albums

import tech.ducletran.travelgalleryupgrade.photos.Photo
import tech.ducletran.travelgalleryupgrade.utils.DateUtils
import java.util.Date

class AlbumsRepo(
    private val albumsDao: AlbumsDao
) {

    suspend fun addAlbum(album: Album) = albumsDao.addAlbum(album.copy(
        dateCreated = DateUtils.getCurrentDateString(DateUtils.FORMAT_DATE_DETAILS),
        dateUpdated = DateUtils.getCurrentDateString(DateUtils.FORMAT_DATE_DETAILS)
    ))

    suspend fun updateAlbum(album: Album) = albumsDao.updateAlbum(album.copy(
        dateUpdated = DateUtils.convertDateToString(Date(),DateUtils.FORMAT_DATE_DETAILS)
    ))

    suspend fun removeAlbum(albumId: Long) {
        albumsDao.removeAlbum(albumId)
        albumsDao.removeAlbumAndPhoto(albumId)
    }

    suspend fun removePhotoFromAlbum(photoId: Long, albumId: Long) {
        albumsDao.removePhotoFromAlbum(photoId, albumId)
        albumsDao.notifyAlbumUpdated(albumId, DateUtils.getCurrentDateString(DateUtils.FORMAT_DATE_DETAILS))
    }

    suspend fun addPhotosToAlbum(photos: List<Photo>, albumId: Long) {
        val photosAndAlbums = photos.map { AlbumAndPhoto(it.id, albumId) }
        albumsDao.addPhotosToAlbum(photosAndAlbums)
        albumsDao.notifyAlbumUpdated(albumId, DateUtils.getCurrentDateString(DateUtils.FORMAT_DATE_DETAILS))
    }

    fun getPhotosFromAlbum(albumId: Long) = albumsDao.getPhotosFromAlbum(albumId)

    fun getAllAlbums() = albumsDao.getAllAlbums()

    fun getAlbumById(albumId: Long) = albumsDao.getAlbumById(albumId)
}