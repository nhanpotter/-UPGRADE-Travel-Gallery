package tech.ducletran.travelgalleryupgrade.photodetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.MenuItem
import android.view.Menu
import android.view.MenuInflater
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_photo_details.view.photo
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.ducletran.travelgalleryupgrade.R
import tech.ducletran.travelgalleryupgrade.ext.nonNull
import tech.ducletran.travelgalleryupgrade.photos.Photo

class PhotoDetailsFragment : Fragment() {

    private val photoDetailsViewModel by viewModel<PhotoDetailsViewModel>()
    private lateinit var rootView: View
    private val safeArgs: PhotoDetailsFragmentArgs by navArgs()

    private lateinit var peopleMenuItem: MenuItem
    private lateinit var foodMenuItem: MenuItem
    private lateinit var favoriteMenuItem: MenuItem

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        rootView = inflater.inflate(R.layout.fragment_photo_details, container, false)

        photoDetailsViewModel.message
            .observe(viewLifecycleOwner, Observer {
            })

        photoDetailsViewModel.photo
            .nonNull()
            .observe(viewLifecycleOwner, Observer {
                setPhotoUI(it)
            })

        val currentPhoto = photoDetailsViewModel.photo.value
        if (currentPhoto == null) {
            photoDetailsViewModel.loadPhoto(safeArgs.photoId)
        } else {
            setPhotoUI(currentPhoto)
        }

        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_photo_details, menu)
        peopleMenuItem = menu.findItem(R.id.actionPeople)
        favoriteMenuItem = menu.findItem(R.id.actionFavorite)
        foodMenuItem = menu.findItem(R.id.actionFood)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.actionFavorite -> {
                photoDetailsViewModel.photo.value?.let {
                    photoDetailsViewModel.setFavorite(it.id, !it.isFavorite)
                }
                true
            }
            R.id.actionFood -> {
                photoDetailsViewModel.photo.value?.let {
                    photoDetailsViewModel.setFood(it.id, !it.isFood)
                }
                true
            }
            R.id.actionPeople -> {
                photoDetailsViewModel.photo.value?.let {
                    photoDetailsViewModel.setFriend(it.id, !it.isFriend)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setPhotoUI(photo: Photo) {
        Glide.with(requireContext())
            .load(photo.url)
            .into(rootView.photo)
        peopleMenuItem.icon = resources.getDrawable(
            if (photo.isFriend) R.drawable.ic_people_filled else R.drawable.ic_people)
        favoriteMenuItem.icon = resources.getDrawable(
            if (photo.isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite)
        foodMenuItem.icon = resources.getDrawable(
            if (photo.isFood) R.drawable.ic_food_filled else R.drawable.ic_food)
    }
}