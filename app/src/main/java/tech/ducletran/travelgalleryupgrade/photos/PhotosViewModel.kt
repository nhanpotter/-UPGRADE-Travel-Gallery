package tech.ducletran.travelgalleryupgrade.photos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import tech.ducletran.travelgalleryupgrade.ext.getDefaultSchedulers
import timber.log.Timber

class PhotosViewModel(
    private val photosService: PhotosService
): ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val mutablePhotos = MutableLiveData<List<Photo>>()
    val photos: LiveData<List<Photo>> = mutablePhotos
    private val mutableLoading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = mutableLoading

    fun loadAllPhotos() {
        compositeDisposable += photosService.loadAllPhotos()
            .getDefaultSchedulers()
            .doOnSubscribe {
                mutableLoading.value = true
            }.subscribe({
                mutablePhotos.value = it
                Timber.d("LOG - Loaded ${it.size} photos.")
                mutableLoading.value = false
            }, {
                Timber.e(it, "Error loading all photos")
                mutableLoading.value = false
            })
    }

    fun addPhotos(photos: List<Photo>) {

        compositeDisposable += photosService.addPhotos(photos)
            .getDefaultSchedulers()
            .doOnSubscribe {
                mutableLoading.value = true
            }.doFinally {
                mutableLoading.value = false
            }.subscribe({
                Timber.d("LOG - Successfully inserted ${photos.size} photos")
            },{
                Timber.e("Error inserting ${photos.size} photos")
            })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}