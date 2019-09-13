package tech.ducletran.travelgalleryupgrade.utils

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import tech.ducletran.travelgalleryupgrade.BuildConfig
import tech.ducletran.travelgalleryupgrade.R

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