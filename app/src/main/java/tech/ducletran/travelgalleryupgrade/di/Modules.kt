package tech.ducletran.travelgalleryupgrade.di

import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tech.ducletran.travelgalleryupgrade.BaseDatabase
import tech.ducletran.travelgalleryupgrade.albums.AlbumNewUpdateViewModel
import tech.ducletran.travelgalleryupgrade.albums.AlbumsRepo
import tech.ducletran.travelgalleryupgrade.albums.AlbumsViewModel
import tech.ducletran.travelgalleryupgrade.customclass.Preference
import tech.ducletran.travelgalleryupgrade.photos.details.PhotoDetailsViewModel
import tech.ducletran.travelgalleryupgrade.photos.PhotosRepo
import tech.ducletran.travelgalleryupgrade.photos.PhotosViewModel
import tech.ducletran.travelgalleryupgrade.customclass.Resource
import tech.ducletran.travelgalleryupgrade.location.LocationViewModel
import tech.ducletran.travelgalleryupgrade.photos.picker.PickPhotosViewModel

val commonModule = module {
    single { Resource() }
    single { Preference() }
}

val singletonModule = module {
    single { PhotosRepo(get()) }
    single { AlbumsRepo(get()) }
}

val databaseModule = module {
    single {
        Room.databaseBuilder(androidApplication(), BaseDatabase::class.java,
            "travel_gallery_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    factory {
        val db: BaseDatabase = get()
        db.photosDao()
    }
    factory {
        val db: BaseDatabase = get()
        db.albumsDao()
    }
}

val viewModelModule = module {
    viewModel { PhotosViewModel(get()) }
    viewModel {
        PhotoDetailsViewModel(
            get(),
            get()
        )
    }
    viewModel { AlbumsViewModel(get(), get(), get()) }
    viewModel { AlbumNewUpdateViewModel(get(), get(), get()) }
    viewModel { LocationViewModel(get()) }
    viewModel { PickPhotosViewModel(get(), get(), get()) }
}