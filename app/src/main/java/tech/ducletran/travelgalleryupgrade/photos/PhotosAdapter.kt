package tech.ducletran.travelgalleryupgrade.photos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_photo.view.photo
import tech.ducletran.travelgalleryupgrade.R

class PhotosAdapter: RecyclerView.Adapter<PhotosViewHolder>() {
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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        return PhotosViewHolder(view)
    }

    override fun getItemCount(): Int = photos.size

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        holder.bind(photos[position], photoClickListener)
    }

}

class PhotosViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun bind(photo: Photo, onPhotoClicked: PhotoClickListener?) {
        Glide.with(itemView.context)
            .load(photo.url)
            .into(itemView.photo)

        itemView.setOnClickListener {
            onPhotoClicked?.onPhotoClicked(photo.id)
        }
    }
}

interface PhotoClickListener {
    fun onPhotoClicked(photoId: Long)
}