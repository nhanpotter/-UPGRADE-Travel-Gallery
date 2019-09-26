package tech.ducletran.travelgalleryupgrade.photos.picker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tech.ducletran.travelgalleryupgrade.R
import tech.ducletran.travelgalleryupgrade.albums.AlbumsRepo
import tech.ducletran.travelgalleryupgrade.customclass.Resource
import tech.ducletran.travelgalleryupgrade.customclass.SingleLiveEvent
import tech.ducletran.travelgalleryupgrade.photos.PhotosRepo

class PickPhotosViewModel(
    private val photosRepo: PhotosRepo,
    private val albumsRepo: AlbumsRepo,
    private val resource: Resource
) : ViewModel() {

    private val photoSelected = mutableSetOf<Long>()
    val message = SingleLiveEvent<String>()
    private val _selectDone = MutableLiveData<Boolean>()
    val selectDone: LiveData<Boolean> = _selectDone

    fun getNumsOfPhotosSelected() = photoSelected.size

    fun selectPhoto(photoId: Long, isSelected: Boolean) {
        if (isSelected)
            photoSelected.add(photoId)
        else
            photoSelected.remove(photoId)
    }

    fun isSelected(photoId: Long) = photoSelected.contains(photoId)

    fun getAllPhotos() = photosRepo.loadAllPhotos()

    fun addPhotosToAlbum(albumId: Long) = viewModelScope.launch {
        albumsRepo.addPhotoIdsToAlbum(photoSelected.toList(), albumId)
        message.value = resource.getString(R.string.message_photos_added_to_album)
        _selectDone.value = true
    }
}