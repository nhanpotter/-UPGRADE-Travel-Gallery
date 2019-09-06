package tech.ducletran.travelgalleryupgrade

import android.app.Application
import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
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
                // List of Modules
            ))
        }
        Timber.plant(Timber.DebugTree())

    }
}