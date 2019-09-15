package tech.ducletran.travelgalleryupgrade.albums

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_album.view.albumCover
import tech.ducletran.travelgalleryupgrade.databinding.ItemAlbumBinding

class AlbumAdapter : RecyclerView.Adapter<AlbumViewHolder>() {

    private val albums = mutableListOf<Album>()
    private var clickListener: AlbumClickedListener? = null

    fun addAlbums(newAlbums: List<Album>) {
        if (albums.isNotEmpty()) albums.clear()
        albums.addAll(newAlbums)
        notifyDataSetChanged()
    }

    fun applyListener(onAlbumClicked: AlbumClickedListener) {
        clickListener = onAlbumClicked
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding = ItemAlbumBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return AlbumViewHolder(binding)
    }

    override fun getItemCount(): Int = albums.size

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(albums[position], clickListener)
    }
}

class AlbumViewHolder(private val binding: ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(album: Album, albumClickListener: AlbumClickedListener?) {
        binding.album = album
        binding.executePendingBindings()

        itemView.albumCover.setOnClickListener {
            albumClickListener?.onAlbumClicked(album.id)
        }
    }
}

interface AlbumClickedListener {
    fun onAlbumClicked(albumId: Long)
}