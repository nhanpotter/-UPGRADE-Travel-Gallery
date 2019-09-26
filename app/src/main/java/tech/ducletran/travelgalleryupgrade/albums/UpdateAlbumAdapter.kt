package tech.ducletran.travelgalleryupgrade.albums

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_album_photo_remove.view.removePhotoButton
import tech.ducletran.travelgalleryupgrade.databinding.ItemAlbumPhotoRemoveBinding
import tech.ducletran.travelgalleryupgrade.photos.Photo

class UpdateAlbumAdapter : ListAdapter<Photo, UpdateAlbumViewHolder>(AlbumDUpdateDiffUtil()) {
    private var onPhotoRemoved: RemovePhotoListener? = null

    fun applyListener(photoRemoveListener: RemovePhotoListener) {
        onPhotoRemoved = photoRemoveListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpdateAlbumViewHolder {
        val binding = ItemAlbumPhotoRemoveBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return UpdateAlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UpdateAlbumViewHolder, position: Int) {
        holder.bind(getItem(position), onPhotoRemoved)
    }
}

class UpdateAlbumViewHolder(
    private val binding: ItemAlbumPhotoRemoveBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(photo: Photo, removePhotoListener: RemovePhotoListener?) {
        binding.photo = photo

        itemView.removePhotoButton.setOnClickListener {
            removePhotoListener?.removePhoto(photo.id)
        }
    }
}

class AlbumDUpdateDiffUtil : DiffUtil.ItemCallback<Photo>() {
    override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean =
            oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean =
            oldItem == newItem
}

interface RemovePhotoListener {
    fun removePhoto(photoId: Long)
}