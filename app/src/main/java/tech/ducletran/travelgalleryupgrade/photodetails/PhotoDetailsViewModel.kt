package tech.ducletran.travelgalleryupgrade.photodetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tech.ducletran.travelgalleryupgrade.R
import tech.ducletran.travelgalleryupgrade.photos.PhotosRepo
import tech.ducletran.travelgalleryupgrade.utils.Resource
import tech.ducletran.travelgalleryupgrade.utils.SingleLiveEvent

class PhotoDetailsViewModel(
    private val photosRepo: PhotosRepo,
    private val resource: Resource
) : ViewModel() {

    val message = SingleLiveEvent<String>()

    fun loadPhoto(photoId: Long) = photosRepo.loadPhoto(photoId)

    fun removePhoto(photoId: Long) = viewModelScope.launch {
        photosRepo.removePhoto(photoId)
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