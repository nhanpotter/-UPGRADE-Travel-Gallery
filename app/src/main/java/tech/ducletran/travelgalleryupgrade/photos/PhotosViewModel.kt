package tech.ducletran.travelgalleryupgrade.photos

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import tech.ducletran.travelgalleryupgrade.customclass.SingleLiveEvent

class PhotosViewModel(
    private val photosRepo: PhotosRepo
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    val message = SingleLiveEvent<String>()

    val photos: LiveData<List<Photo>> = photosRepo.loadAllPhotos()

    fun addPhotos(photos: List<Photo>) = viewModelScope.launch {
        photosRepo.addPhotos(photos)
        message.value = "${photos.size} photos added!"
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}