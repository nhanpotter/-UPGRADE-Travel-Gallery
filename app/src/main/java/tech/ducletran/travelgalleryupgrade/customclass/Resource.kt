package tech.ducletran.travelgalleryupgrade.customclass

import androidx.annotation.StringRes
import tech.ducletran.travelgalleryupgrade.BaseApplication
import tech.ducletran.travelgalleryupgrade.ext.notNull

class Resource {

    private val context by lazy {
        BaseApplication.appContext
    }

    fun getString(@StringRes resId: Int) = context?.getString(resId).notNull()

    fun getString(@StringRes resId: Int, vararg args: Any?) = context?.getString(resId, args).notNull()
}