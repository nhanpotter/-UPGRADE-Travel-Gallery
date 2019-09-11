package tech.ducletran.travelgalleryupgrade

import android.app.Application
import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import tech.ducletran.travelgalleryupgrade.di.commonModule
import tech.ducletran.travelgalleryupgrade.di.databaseModule
import tech.ducletran.travelgalleryupgrade.di.viewModelModule
import tech.ducletran.travelgalleryupgrade.di.singletonModule
import timber.log.Timber

class BaseApplication : Application() {

    companion object {
        @JvmStatic
        var appContext: Context? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        startKoin {
            androidContext(this@BaseApplication)
            androidLogger()
            modules(listOf(
                commonModule,
                databaseModule,
                viewModelModule,
                singletonModule
            ))
        }
        Timber.plant(Timber.DebugTree())
    }
}