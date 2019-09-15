package tech.ducletran.travelgalleryupgrade.albums

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tech.ducletran.travelgalleryupgrade.photos.PhotosRepo
import tech.ducletran.travelgalleryupgrade.utils.Resource
import tech.ducletran.travelgalleryupgrade.utils.SingleLiveEvent

class AlbumsViewModel(
    private val albumsRepo: AlbumsRepo,
    private val photosRepo: PhotosRepo,
    private val resource: Resource
) : ViewModel() {

    val message = SingleLiveEvent<String>()

    fun getAllAlbums() = albumsRepo.getAllAlbums()

    fun createNewAlbum(album: Album) = viewModelScope.launch {
        albumsRepo.addAlbum(album)
        message.value = "New album - ${album.title} created"
    }

    fun getPhotosFromAlbum(albumId: Long) =
            when (albumId) {
        AlbumConstant.PEOPLE_ID -> photosRepo.loadFriendPhotos()
        AlbumConstant.FOOD_ID -> photosRepo.loadFoodPhotos()
        AlbumConstant.FAVORITE_ID -> photosRepo.loadFavoritePhotos()
        else -> albumsRepo.getPhotosFromAlbum(albumId)
    }
}