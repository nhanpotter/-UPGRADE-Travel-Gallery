package tech.ducletran.travelgalleryupgrade.ext

import android.app.Activity
import android.content.Context
import android.view.View
import com.google.android.material.snackbar.Snackbar
import android.view.inputmethod.InputMethodManager

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

fun View.hideKeyboard(activity: Activity?) {
    val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE)
    if (imm is InputMethodManager)
        imm.hideSoftInputFromWindow(windowToken, 0)
}