package tech.ducletran.travelgalleryupgrade.photos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import tech.ducletran.travelgalleryupgrade.databinding.ItemPhotoBinding

class PhotosAdapter : ListAdapter<Photo, PhotosViewHolder>(PhotoDiffUtil()) {
    private var photoClickListener: PhotoClickListener? = null

    fun applyListener(onPhotoClicked: PhotoClickListener) {
        photoClickListener = onPhotoClicked
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        val binding = ItemPhotoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return PhotosViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        holder.bind(getItem(position), photoClickListener)
    }
}

class PhotoDiffUtil : DiffUtil.ItemCallback<Photo>() {
    override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean = oldItem == newItem
}

class PhotosViewHolder(private val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(photo: Photo, onPhotoClicked: PhotoClickListener?) {
        binding.photo = photo
        binding.executePendingBindings()

        itemView.setOnClickListener {
            onPhotoClicked?.onPhotoClicked(photo.id)
        }
    }
}

interface PhotoClickListener {
    fun onPhotoClicked(photoId: Long)
}