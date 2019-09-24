package tech.ducletran.travelgalleryupgrade.albums

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tech.ducletran.travelgalleryupgrade.photos.PhotosRepo
import tech.ducletran.travelgalleryupgrade.utils.Resource
import tech.ducletran.travelgalleryupgrade.utils.SingleLiveEvent

class AlbumNewUpdateViewModel(
    private val photosRepo: PhotosRepo,
    private val albumsRepo: AlbumsRepo,
    private val resource: Resource
) : ViewModel() {

    private val _albumCreatedOrUpdated = MutableLiveData<Boolean>()
    val albumCreatedOrUpdated: LiveData<Boolean> = _albumCreatedOrUpdated
    val message = SingleLiveEvent<String>()
    private val _coverImage = MutableLiveData<String>()
    val coverImage: LiveData<String> = _coverImage

    fun getAllPhotos() = photosRepo.loadAllPhotos()

    fun getAlbumById(albumId: Long): LiveData<Album> = albumsRepo.getAlbumById(albumId)

    fun createNewAlbum(album: Album) = viewModelScope.launch {
        albumsRepo.addAlbum(album)
        message.value = "New album - ${album.title} created"
        _albumCreatedOrUpdated.value = true
    }

    fun setCoverImage(coverImage: String) {
        if (_coverImage.value.isNullOrEmpty())
            _coverImage.value = coverImage
    }

    fun updateAlbum(album: Album) = viewModelScope.launch {
        albumsRepo.updateAlbum(album)
        message.value = "Album - ${album.title} updated"
        _albumCreatedOrUpdated.value = true
    }

    fun pickCoverImage(photoId: Long) = viewModelScope.launch {
        _coverImage.value = photosRepo.getPhotoWithId(photoId).url
    }
}