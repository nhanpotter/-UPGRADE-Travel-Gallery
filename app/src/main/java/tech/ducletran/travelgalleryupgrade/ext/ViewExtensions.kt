package tech.ducletran.travelgalleryupgrade.ext

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.snackbar(
    message: String,
    length: Int = Snackbar.LENGTH_SHORT,
    backgroundColor: Int? = null
) {
    val snackBar = Snackbar.make(this, message, length)
    backgroundColor?.let {
        snackBar.view.setBackgroundColor(it)
    }
    snackBar.show()
}