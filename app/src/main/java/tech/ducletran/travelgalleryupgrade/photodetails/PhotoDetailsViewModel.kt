package tech.ducletran.travelgalleryupgrade.photodetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
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

    fun setFriend(photoId: Long, isFriend: Boolean) {
        if (photoId == -1L) return

        compositeDisposable += photosService.setPhotoFriend(photoId, isFriend)
            .getDefaultSchedulers()
            .subscribe({
                message.value = if (isFriend) resource.getString(R.string.message_photo_added_to_friend) else
                    resource.getString(R.string.message_photo_removed_from_friend)
            }, {
                message.value = if (isFriend) resource.getString(R.string.error_message_photo_add_to_friend) else
                    resource.getString(R.string.error_message_photo_remove_from_friend)
                Timber.e(it, "Error setting photo with ID $photoId for friend feature $isFriend")
            })
    }

    fun setFavorite(photoId: Long, isFavorite: Boolean) {
        if (photoId == -1L) return

        compositeDisposable += photosService.setPhotoFriend(photoId, isFavorite)
            .getDefaultSchedulers()
            .subscribe({
                message.value = if (isFavorite) resource.getString(R.string.message_photo_added_to_favorite) else
                    resource.getString(R.string.message_photo_removed_from_favorite)
            }, {
                message.value = if (isFavorite) resource.getString(R.string.error_message_photo_add_to_favorite) else
                    resource.getString(R.string.error_message_photo_remove_from_favorite)
                Timber.e(it, "Error setting photo with ID $photoId for friend feature $isFavorite")
            })
    }

    fun setFood(photoId: Long, isFood: Boolean) {
        if (photoId == -1L) return

        compositeDisposable += photosService.setPhotoFriend(photoId, isFood)
            .getDefaultSchedulers()
            .subscribe({
                message.value = if (isFood) resource.getString(R.string.message_photo_added_to_food) else
                    resource.getString(R.string.message_photo_removed_from_food)
            }, {
                message.value = if (isFood) resource.getString(R.string.error_message_photo_add_to_food) else
                    resource.getString(R.string.error_message_photo_remove_from_food)
                Timber.e(it, "Error setting photo with ID $photoId for friend feature $isFood")
            })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}