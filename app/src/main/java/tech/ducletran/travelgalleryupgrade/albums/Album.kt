package tech.ducletran.travelgalleryupgrade.albums

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Album(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String = "",
    val coverImage: String = "",
    val dateCreated: String = "",
    val dateUpdated: String = "",
    val location: String = "",
    val type: String = AlbumConstant.TYPE_OTHERS,
    val description: String = ""
)