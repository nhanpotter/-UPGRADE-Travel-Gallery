package tech.ducletran.travelgalleryupgrade.utils

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import tech.ducletran.travelgalleryupgrade.BuildConfig
import tech.ducletran.travelgalleryupgrade.R
import java.text.DecimalFormat

@BindingAdapter("hideIfEmpty")
fun hideIfEmpty(view: View, text: String?) {
    view.isVisible = !text.isNullOrEmpty()
}

@BindingAdapter("photoUrl")
fun photoUrl(imageView: ImageView, url: String?) {
    Glide.with(imageView.context)
        .load(url)
        .into(imageView)
}

@BindingAdapter("isPhotoFavorite")
fun isPhotoFavorite(imageButton: ImageButton, isFavorite: Boolean) {
    imageButton.setImageDrawable(imageButton.context.resources.getDrawable(
        if (isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite
    ))
}

@BindingAdapter("isPhotoFood")
fun isPhotoFood(imageButton: ImageButton, isFood: Boolean) {
    imageButton.setImageDrawable(imageButton.context.resources.getDrawable(
        if (isFood) R.drawable.ic_food_filled else R.drawable.ic_food
    ))
}

@BindingAdapter("isPhotoFriend")
fun isPhotoFriend(imageButton: ImageButton, isFriend: Boolean) {
    imageButton.setImageDrawable(imageButton.context.resources.getDrawable(
        if (isFriend) R.drawable.ic_people_filled else R.drawable.ic_people
    ))
}

@BindingAdapter("latitude", "longitude")
fun loadLocationImage(imageView: ImageView, latitude: String?, longitude: String?) {
    if (latitude.isNullOrEmpty() || longitude.isNullOrEmpty()) {
        imageView.isVisible = false
        return
    }
    imageView.isVisible = true
    val baseUrl = "https://api.mapbox.com/v4/mapbox.emerald/pin-l-marker+673ab7"
    val location = "($longitude,$latitude)/$longitude,$latitude"
    val mapboxPreviewImage = "$baseUrl$location,15/900x500.png?access_token=${BuildConfig.MAPBOX_KEY}"

    Glide.with(imageView.context)
        .load(mapboxPreviewImage)
        .into(imageView)
}

@BindingAdapter("latitude", "longitude")
fun loadLocationText(textView: TextView, latitude: String?, longitude: String?) {
    if (latitude.isNullOrEmpty() || longitude.isNullOrEmpty()) {
        textView.text = "No location"
        return
    }

    val doubleFormatter = DecimalFormat("#0.00")
    textView.isVisible = true
    textView.text = "( ${doubleFormatter.format(latitude.toDouble())} - ${doubleFormatter.format(longitude.toDouble())})"
}

@BindingAdapter("dateTaken")
fun showDateTaken(textView: TextView, date: String?) {
    if (date.isNullOrEmpty())
        return

    val dateTaken = DateUtils.convertDateBetweenFormats(date, DateUtils.FORMAT_DATE_DETAILS, DateUtils.FORMAT_DATE_SIMPLE)
    textView.text = "Date Taken: $dateTaken"
}

@BindingAdapter("size")
fun showSize(textView: TextView, size: String?) {
    if (size.isNullOrEmpty())
        return

    val sizeNumber = size.toInt()
    val sizeString = when {
        sizeNumber > 1000000 -> "${sizeNumber / 1000000}MB"
        sizeNumber in 1000..99999 -> "${sizeNumber / 1000}KB"
        else -> "${sizeNumber}B"
    }

    textView.text = "Size: $sizeString"
}

@BindingAdapter("isSelected")
fun selectPhoto(imageView: ImageView, isSelected: Boolean) {
    imageView.isVisible = isSelected
}