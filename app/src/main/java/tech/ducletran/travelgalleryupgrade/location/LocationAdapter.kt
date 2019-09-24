package tech.ducletran.travelgalleryupgrade.location

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mapbox.api.geocoding.v5.models.CarmenFeature
import tech.ducletran.travelgalleryupgrade.databinding.ItemLocationBinding

class LocationAdapter : ListAdapter<CarmenFeature, LocationViewHolder>(LocationDiffUtil()) {

    private var locationClickListener: LocationClickListener? = null

    fun applyListener(onLocationCLicked: LocationClickListener) {
        locationClickListener = onLocationCLicked
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val binding = ItemLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        holder.bind(getItem(position), locationClickListener)
    }
}

class LocationViewHolder(
    private val binding: ItemLocationBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(carmenFeature: CarmenFeature, onLocationSelected: LocationClickListener?) {
        carmenFeature.placeName()?.split(",")?.let {
            val pair = Pair(it.first(), it.subList(1, it.size).joinToString(", "))
            binding.places = pair
            binding.executePendingBindings()

            itemView.setOnClickListener {
                onLocationSelected?.onLocationSelected(pair.first)
            }
        }
    }
}

class LocationDiffUtil : DiffUtil.ItemCallback<CarmenFeature>() {
    override fun areItemsTheSame(oldItem: CarmenFeature, newItem: CarmenFeature): Boolean =
            oldItem.id() == newItem.id()

    override fun areContentsTheSame(oldItem: CarmenFeature, newItem: CarmenFeature): Boolean =
            oldItem.placeName() == newItem.placeName()
}

interface LocationClickListener {
    fun onLocationSelected(location: String)
}