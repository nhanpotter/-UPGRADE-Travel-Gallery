package tech.ducletran.travelgalleryupgrade.photodetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_photo_details.view.favoriteButton
import kotlinx.android.synthetic.main.fragment_photo_details.view.infoButton
import kotlinx.android.synthetic.main.fragment_photo_details.view.friendButton
import kotlinx.android.synthetic.main.fragment_photo_details.view.foodButton
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.ducletran.travelgalleryupgrade.R
import tech.ducletran.travelgalleryupgrade.databinding.FragmentPhotoDetailsBinding
import tech.ducletran.travelgalleryupgrade.ext.nonNull
import tech.ducletran.travelgalleryupgrade.photos.Photo

class PhotoDetailsFragment : Fragment() {

    private val photoDetailsViewModel by viewModel<PhotoDetailsViewModel>()
    private lateinit var rootView: View
    private val safeArgs: PhotoDetailsFragmentArgs by navArgs()

    private lateinit var binding: FragmentPhotoDetailsBinding
    private lateinit var photo: Photo

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_photo_details, container, false)
        rootView = binding.root

        photoDetailsViewModel.message
            .observe(viewLifecycleOwner, Observer {
                Snackbar.make(rootView, it, Snackbar.LENGTH_SHORT)
            })

        photoDetailsViewModel.loadPhoto(safeArgs.photoId)
            .nonNull()
            .observe(viewLifecycleOwner, Observer {
                binding.photo = it
                binding.executePendingBindings()
                photo = it
            })

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rootView.infoButton.setOnClickListener {
            findNavController(rootView).navigate(PhotoDetailsFragmentDirections
                .actionPhotoDetailsToPhotoInfo(safeArgs.photoId))
        }
        rootView.favoriteButton.setOnClickListener {
            photoDetailsViewModel.setFavorite(safeArgs.photoId, !photo.isFavorite)
        }
        rootView.friendButton.setOnClickListener {
            photoDetailsViewModel.setFriend(safeArgs.photoId, !photo.isFriend)
        }
        rootView.foodButton.setOnClickListener {
            photoDetailsViewModel.setFood(safeArgs.photoId, !photo.isFood)
        }
    }
}