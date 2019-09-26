package tech.ducletran.travelgalleryupgrade.photos.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tech.ducletran.travelgalleryupgrade.R
import tech.ducletran.travelgalleryupgrade.photos.PhotosRepo
import tech.ducletran.travelgalleryupgrade.customclass.Resource
import tech.ducletran.travelgalleryupgrade.customclass.SingleLiveEvent
import tech.ducletran.travelgalleryupgrade.photos.Photo

class PhotoDetailsViewModel(
    private val photosRepo: PhotosRepo,
    private val resource: Resource
) : ViewModel() {

    val message = SingleLiveEvent<String>()
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun loadPhoto(photoId: Long) = photosRepo.loadPhoto(photoId)

    fun removePhoto(photoId: Long) = viewModelScope.launch {
        photosRepo.removePhoto(photoId)
    }

    fun updatePhoto(photo: Photo) = viewModelScope.launch {
        _loading.value = true
        photosRepo.updatePhoto(photo)
        _loading.value = false
        message.value = resource.getString(R.string.message_photo_updated)
    }

    fun setFriend(photoId: Long, isFriend: Boolean) = viewModelScope.launch {
        photosRepo.setPhotoFriend(photoId, isFriend)
        message.value = if (isFriend) resource.getString(R.string.message_photo_added_to_friend) else
            resource.getString(R.string.message_photo_removed_from_friend)
    }

    fun setFavorite(photoId: Long, isFavorite: Boolean) = viewModelScope.launch {
        photosRepo.setPhotoFavorite(photoId, isFavorite)
        message.value = if (isFavorite) resource.getString(R.string.message_photo_added_to_favorite) else
            resource.getString(R.string.message_photo_removed_from_favorite)
    }

    fun setFood(photoId: Long, isFood: Boolean) = viewModelScope.launch {
        photosRepo.setPhotoFood(photoId, isFood)
        message.value = if (isFood) resource.getString(R.string.message_photo_added_to_food) else
            resource.getString(R.string.message_photo_removed_from_food)
    }
}