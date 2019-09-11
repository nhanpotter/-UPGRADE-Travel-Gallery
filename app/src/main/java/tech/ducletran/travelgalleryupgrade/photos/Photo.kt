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
    val dateTaken: String = "",
    val latitude: String = "",
    val longitude: String = "",
    val size: String = "",
    val title: String = "",
    val description: String = "",
    val width: String = "",
    val height: String = ""
)