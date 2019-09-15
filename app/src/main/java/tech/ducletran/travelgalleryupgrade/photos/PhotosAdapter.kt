package tech.ducletran.travelgalleryupgrade.photos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tech.ducletran.travelgalleryupgrade.databinding.ItemPhotoBinding

class PhotosAdapter : RecyclerView.Adapter<PhotosViewHolder>() {
    private val photos = mutableListOf<Photo>()
    private var photoClickListener: PhotoClickListener? = null

    fun addPhotos(photosList: List<Photo>) {
        if (photos.isNotEmpty()) photos.clear()
        photos.addAll(photosList)
        notifyDataSetChanged()
    }

    fun applyListener(onPhotoClicked: PhotoClickListener) {
        photoClickListener = onPhotoClicked
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        val binding = ItemPhotoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return PhotosViewHolder(binding)
    }

    override fun getItemCount(): Int = photos.size

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        holder.bind(photos[position], photoClickListener)
    }
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