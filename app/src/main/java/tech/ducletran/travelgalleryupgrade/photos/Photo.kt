package tech.ducletran.travelgalleryupgrade.photos

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Photo(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val url: String = "",
    val thumbnail: String = "",
    val isFavorite: Boolean = false,
    val isFood: Boolean = false,
    val isFriend: Boolean = false,
    var dateTaken: String = "",
    var latitude: String = "",
    var longitude: String = "",
    val size: String = "",
    var title: String = "",
    var description: String = "",
    var placeName: String = "",
    val width: String = "",
    val height: String = ""
)