package tech.ducletran.travelgalleryupgrade.photos

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch

class PhotosViewModel(
    private val photosService: PhotosService
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val photos: LiveData<List<Photo>> = photosService.loadAllPhotos()

    fun addPhotos(photos: List<Photo>) = viewModelScope.launch {
        photosService.addPhotos(photos)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}