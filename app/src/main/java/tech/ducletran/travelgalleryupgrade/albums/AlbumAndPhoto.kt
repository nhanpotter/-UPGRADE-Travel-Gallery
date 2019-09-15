package tech.ducletran.travelgalleryupgrade.albums

import androidx.room.Entity
import androidx.room.ForeignKey
import tech.ducletran.travelgalleryupgrade.photos.Photo

@Entity(
    primaryKeys = ["photoId", "albumId"],
    foreignKeys = [
        ForeignKey(entity = Photo::class, parentColumns = ["id"],
            childColumns = ["photoId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Album::class, parentColumns = ["id"],
            childColumns = ["albumId"], onDelete = ForeignKey.CASCADE)
    ])
class AlbumAndPhoto(
    val photoId: Long,
    val albumId: Long
)