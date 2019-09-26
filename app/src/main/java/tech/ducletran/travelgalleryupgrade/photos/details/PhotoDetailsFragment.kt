package tech.ducletran.travelgalleryupgrade.photos.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_photo_details.view.favoriteButton
import kotlinx.android.synthetic.main.fragment_photo_details.view.infoButton
import kotlinx.android.synthetic.main.fragment_photo_details.view.friendButton
import kotlinx.android.synthetic.main.fragment_photo_details.view.foodButton
import kotlinx.android.synthetic.main.fragment_photo_details.view.shareButton
import kotlinx.android.synthetic.main.fragment_photo_details.view.photoView
import kotlinx.android.synthetic.main.fragment_photo_details.view.bottomTaskLayout
import kotlinx.android.synthetic.main.fragment_photo_details.view.toolbar
import kotlinx.android.synthetic.main.fragment_photo_details.view.toolbarLayout
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.ducletran.travelgalleryupgrade.R
import tech.ducletran.travelgalleryupgrade.databinding.FragmentPhotoDetailsBinding
import tech.ducletran.travelgalleryupgrade.ext.nonNull
import tech.ducletran.travelgalleryupgrade.ext.snackbar
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_photo_details, container, false)
        rootView = binding.root

        setupToolbar()

        photoDetailsViewModel.message
            .observe(viewLifecycleOwner, Observer {
                rootView.snackbar(it)
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

        rootView.shareButton.setOnClickListener {
            val shareImageIntent = Intent()
            with(shareImageIntent) {
                action = Intent.ACTION_SEND
                type = "image/*"
                putExtra(Intent.EXTRA_STREAM, Uri.parse(photo.url))
                startActivity(Intent.createChooser(this, "Share Image"))
            }
        }

        rootView.photoView.setOnViewTapListener { _, _, _ ->
            rootView.toolbarLayout.visibility =
                if (rootView.bottomTaskLayout.isVisible) View.INVISIBLE else View.VISIBLE
            rootView.bottomTaskLayout.isVisible = !rootView.bottomTaskLayout.isVisible
        }

        rootView.infoButton.setOnClickListener {
            findNavController(rootView).navigate(
                PhotoDetailsFragmentDirections.actionPhotoDetailsToPhotoInfo(
                    safeArgs.photoId
                )
            )
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

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.let {
            if (it is AppCompatActivity) {
                it.supportActionBar?.show()
            }
        }
    }

    private fun setupToolbar() {
        activity?.let {
            if (it is AppCompatActivity) {
                it.supportActionBar?.hide()
            }
        }

        rootView.toolbar.title = ""
        rootView.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        rootView.toolbar.overflowIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_more_vert)
        rootView.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        rootView.toolbar.inflateMenu(R.menu.fragment_photo_details)
        rootView.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.actionRemovePhoto -> {
                    photoDetailsViewModel.removePhoto(safeArgs.photoId)
                    activity?.onBackPressed()
                    true
                }
                R.id.actionUseAsPhoto -> {
                    val usePhotoIntent = Intent()
                    with(usePhotoIntent) {
                        action = Intent.ACTION_ATTACH_DATA
                        type = "image/*"
                        addCategory(Intent.CATEGORY_DEFAULT)
                        putExtra(Intent.EXTRA_STREAM, Uri.parse(photo.url))
                        startActivity(Intent.createChooser(this, "Set As"))
                    }
                    true
                }
                else -> super.onOptionsItemSelected(it)
            }
        }
    }
}