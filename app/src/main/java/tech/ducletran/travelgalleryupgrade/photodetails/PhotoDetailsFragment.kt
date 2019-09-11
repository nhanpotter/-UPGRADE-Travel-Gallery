package tech.ducletran.travelgalleryupgrade.photodetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_photo_details.view.photo
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.ducletran.travelgalleryupgrade.R
import tech.ducletran.travelgalleryupgrade.ext.nonNull

class PhotoDetailsFragment: Fragment() {

    private val photoDetailsViewModel by viewModel<PhotoDetailsViewModel>()
    private lateinit var rootView: View
    private val safeArgs: PhotoDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_photo_details, container, false)

        photoDetailsViewModel.message
            .observe(viewLifecycleOwner, Observer {

            })

        photoDetailsViewModel.photo
            .nonNull()
            .observe(viewLifecycleOwner, Observer {
                Glide.with(requireContext())
                    .load(it.url)
                    .into(rootView.photo)
            })

        val currentPhoto = photoDetailsViewModel.photo.value
        if (currentPhoto == null) {
            photoDetailsViewModel.loadPhoto(safeArgs.photoId)
        } else {
            Glide.with(requireContext())
                .load(currentPhoto.url)
                .into(rootView.photo)
        }

        return rootView
    }
}