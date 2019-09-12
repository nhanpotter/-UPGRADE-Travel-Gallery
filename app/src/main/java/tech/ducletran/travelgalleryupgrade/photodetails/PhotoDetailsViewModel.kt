package tech.ducletran.travelgalleryupgrade.photodetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.coroutines.launch
import tech.ducletran.travelgalleryupgrade.R
import tech.ducletran.travelgalleryupgrade.ext.getDefaultSchedulers
import tech.ducletran.travelgalleryupgrade.photos.Photo
import tech.ducletran.travelgalleryupgrade.photos.PhotosService
import tech.ducletran.travelgalleryupgrade.utils.Resource
import tech.ducletran.travelgalleryupgrade.utils.SingleLiveEvent
import timber.log.Timber

class PhotoDetailsViewModel(
    private val photosService: PhotosService,
    private val resource: Resource
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val mutablePhoto = MutableLiveData<Photo>()
    val photo: LiveData<Photo> = mutablePhoto
    val message = SingleLiveEvent<String>()

    fun loadPhoto(photoId: Long) {
        if (photoId == -1L) return

        compositeDisposable += photosService.loadPhoto(photoId)
            .distinctUntilChanged()
            .getDefaultSchedulers()
            .subscribe({
                mutablePhoto.value = it
            }, {
                Timber.e(it, "Error loading photo with ID $photoId")
            })
    }

    fun setFriend(photoId: Long, isFriend: Boolean) = viewModelScope.launch {
            photosService.setPhotoFriend(photoId, isFriend)
            message.value = if (isFriend) resource.getString(R.string.message_photo_added_to_friend) else
                resource.getString(R.string.message_photo_removed_from_friend)
        }

    fun setFavorite(photoId: Long, isFavorite: Boolean) = viewModelScope.launch {
        photosService.setPhotoFavorite(photoId, isFavorite)
        message.value = if (isFavorite) resource.getString(R.string.message_photo_added_to_favorite) else
            resource.getString(R.string.message_photo_removed_from_favorite)
    }

    fun setFood(photoId: Long, isFood: Boolean) = viewModelScope.launch {
        photosService.setPhotoFood(photoId, isFood)
        message.value = if (isFood) resource.getString(R.string.message_photo_added_to_food) else
            resource.getString(R.string.message_photo_removed_from_food)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}