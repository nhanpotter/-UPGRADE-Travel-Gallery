package tech.ducletran.travelgalleryupgrade.albums

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean> = _progress
    private val _albumDeleted = MutableLiveData<Boolean>()
    val albumDeleted: LiveData<Boolean> = _albumDeleted
    private val _albumCreatedOrUpdated = MutableLiveData<Boolean>()
    val albumCreatedOrUpdated: LiveData<Boolean> = _albumCreatedOrUpdated

    fun getAllAlbums() = albumsRepo.getAllAlbums()

    fun createNewAlbum(album: Album) = viewModelScope.launch {
        albumsRepo.addAlbum(album)
        message.value = "New album - ${album.title} created"
        _albumCreatedOrUpdated.value = true
    }

    fun updateAlbum(album: Album) = viewModelScope.launch {
        albumsRepo.updateAlbum(album)
        message.value = "Album - ${album.title} updated"
        _albumCreatedOrUpdated.value = true
    }

    fun getAlbumById(albumId: Long): LiveData<Album> = albumsRepo.getAlbumById(albumId)

    fun getPhotosFromAlbum(albumId: Long) = when (albumId) {
        AlbumConstant.PEOPLE_ID -> photosRepo.loadFriendPhotos()
        AlbumConstant.FOOD_ID -> photosRepo.loadFoodPhotos()
        AlbumConstant.FAVORITE_ID -> photosRepo.loadFavoritePhotos()
        else -> albumsRepo.getPhotosFromAlbum(albumId)
    }

    fun removeAlbum(albumId: Long) = viewModelScope.launch {
        _progress.value = true
        albumsRepo.removeAlbum(albumId)
        _progress.value = false
        _albumDeleted.value = true
    }
}