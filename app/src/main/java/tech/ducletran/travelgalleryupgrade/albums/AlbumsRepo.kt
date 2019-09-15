package tech.ducletran.travelgalleryupgrade.albums

import tech.ducletran.travelgalleryupgrade.photos.Photo

class AlbumsRepo(
    private val albumsDao: AlbumsDao
) {

    suspend fun addAlbum(album: Album) = albumsDao.addAlbum(album)

    suspend fun updateAlbum(album: Album) = albumsDao.updateAlbum(album)

    suspend fun removeAlbum(albumId: Long) = albumsDao.removeAlbum(albumId)

    suspend fun removePhotoFromAlbum(photoId: Long, albumId: Long) =
            albumsDao.removePhotoFromAlbum(photoId, albumId)

    suspend fun addPhotosToAlbum(photos: List<Photo>, albumId: Long) {
        val photosAndAlbums = photos.map { AlbumAndPhoto(it.id, albumId) }
        albumsDao.addPhotosToAlbum(photosAndAlbums)
    }

    fun getPhotosFromAlbum(albumId: Long) = albumsDao.getPhotosFromAlbum(albumId)

    fun getAllAlbums() = albumsDao.getAllAlbums()
}