package tech.ducletran.travelgalleryupgrade.photos.picker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tech.ducletran.travelgalleryupgrade.databinding.ItemPhotoPickerBinding
import tech.ducletran.travelgalleryupgrade.photos.Photo

class PhotoPickerAdapter : RecyclerView.Adapter<PhotoPickerViewHolder>() {

    private val photosList = mutableListOf<Pair<Boolean, Photo>>()
    private var onPhotoSelected: PhotoSelectListener? = null

    fun applyListener(photoSelectListener: PhotoSelectListener) {
        onPhotoSelected = photoSelectListener
    }

    fun updatePhoto(position: Int, isSelected: Boolean) {
        val updatedItem = Pair(isSelected, photosList[position].second)
        photosList[position] = updatedItem
        notifyItemChanged(position)
    }

    fun updateList(list: List<Pair<Boolean, Photo>>) {
        if (photosList.isNotEmpty())
            photosList.clear()
        photosList.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = photosList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoPickerViewHolder {
        val binding = ItemPhotoPickerBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoPickerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoPickerViewHolder, position: Int) {
        holder.bind(photosList[position], onPhotoSelected, position)
    }
}

class PhotoPickerViewHolder(private val binding: ItemPhotoPickerBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(pair: Pair<Boolean, Photo>, onPhotoSelectListener: PhotoSelectListener?, position: Int) {
        binding.photo = pair.second
        binding.isSelected = pair.first
        binding.executePendingBindings()

        itemView.setOnClickListener {
            onPhotoSelectListener?.select(pair.second, pair.first, position)
        }
    }
}

interface PhotoSelectListener {
    fun select(photo: Photo, isSelected: Boolean, position: Int)
}