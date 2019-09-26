package tech.ducletran.travelgalleryupgrade.albums

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.MenuInflater
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.dialog_pick_photo.view.photoRecyclerView
import kotlinx.android.synthetic.main.dialog_pick_photo.view.noPhotoTextView
import kotlinx.android.synthetic.main.fragment_album_new_update.view.location
import kotlinx.android.synthetic.main.fragment_album_new_update.view.coverImage
import kotlinx.android.synthetic.main.fragment_album_new_update.view.name
import kotlinx.android.synthetic.main.fragment_album_new_update.view.nameLayout
import kotlinx.android.synthetic.main.fragment_album_new_update.view.photosRemoveGrid
import kotlinx.android.synthetic.main.fragment_album_new_update.view.title
import kotlinx.android.synthetic.main.fragment_album_new_update.view.description
import kotlinx.android.synthetic.main.fragment_album_new_update.view.radioGroup
import kotlinx.android.synthetic.main.fragment_album_new_update.view.locationLayout
import kotlinx.android.synthetic.main.fragment_album_new_update.view.othersRadioButton
import kotlinx.android.synthetic.main.fragment_album_new_update.view.locationRadioButton
import kotlinx.android.synthetic.main.fragment_album_new_update.view.chooseLocationButton
import kotlinx.android.synthetic.main.fragment_album_new_update.view.photoSection
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.ducletran.travelgalleryupgrade.R
import tech.ducletran.travelgalleryupgrade.customclass.GridSpacingItemDecoration
import tech.ducletran.travelgalleryupgrade.customclass.Preference
import tech.ducletran.travelgalleryupgrade.ext.hideKeyboard
import tech.ducletran.travelgalleryupgrade.ext.nonNull
import tech.ducletran.travelgalleryupgrade.ext.notNull
import tech.ducletran.travelgalleryupgrade.ext.snackbar
import tech.ducletran.travelgalleryupgrade.location.LOCATION
import tech.ducletran.travelgalleryupgrade.photos.PhotoClickListener
import tech.ducletran.travelgalleryupgrade.photos.PhotosAdapter

class AlbumNewUpdateFragment : Fragment() {

    private lateinit var rootView: View
    private val safeArgs: AlbumNewUpdateFragmentArgs by navArgs()
    private val albumNewUpdateViewModel by viewModel<AlbumNewUpdateViewModel>()
    private lateinit var album: Album
    private val preference = Preference()
    private val updateAlbumAdapter = UpdateAlbumAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        rootView = inflater.inflate(R.layout.fragment_album_new_update, container, false)

        albumNewUpdateViewModel.message
            .observe(viewLifecycleOwner, Observer {
                rootView.snackbar(it)
            })

        albumNewUpdateViewModel.albumCreatedOrUpdated
            .nonNull()
            .observe(viewLifecycleOwner, Observer {
                rootView.hideKeyboard(activity)
                findNavController(rootView).popBackStack()
            })

        albumNewUpdateViewModel.coverImage
            .nonNull()
            .observe(viewLifecycleOwner, Observer {
                Glide.with(requireContext())
                    .load(it)
                    .error(R.drawable.default_cover_image)
                    .into(rootView.coverImage)
            })

        if (!preference.getString(LOCATION).isNullOrEmpty()) {
            rootView.location.text = preference.getString(LOCATION)
            preference.putString(LOCATION, null)
        }

        if (safeArgs.albumId != -1L) {
            rootView.photosRemoveGrid.adapter = updateAlbumAdapter
            rootView.photosRemoveGrid.layoutManager = GridLayoutManager(requireContext(), 2)
            rootView.photosRemoveGrid.addItemDecoration(GridSpacingItemDecoration(2, 16, false))

            albumNewUpdateViewModel.getPhotosFromAlbum(safeArgs.albumId)
                .nonNull()
                .observe(viewLifecycleOwner, Observer {
                    updateAlbumAdapter.submitList(it)
                    rootView.photoSection.isVisible = it.isNotEmpty()
                })

            rootView.title.text = getString(R.string.updating_album)
            albumNewUpdateViewModel.getAlbumById(safeArgs.albumId)
                .nonNull()
                .observe(viewLifecycleOwner, Observer {
                    album = it
                    rootView.name.setText(it.title)
                    rootView.description.setText(it.description)
                    albumNewUpdateViewModel.setCoverImage(it.coverImage)

                    if (rootView.location.text == getString(R.string.no_location_selected)) {
                        rootView.location.text = it.location
                    }
                    rootView.radioGroup.check(if (it.type == AlbumConstant.TYPE_OTHERS)
                        R.id.othersRadioButton else R.id.locationRadioButton)
                })
        }

        rootView.name.isEnabled = !rootView.locationRadioButton.isChecked

        Glide.with(requireContext())
            .load(albumNewUpdateViewModel.coverImage.value)
            .error(R.drawable.default_cover_image)
            .into(rootView.coverImage)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rootView.name.addTextChangedListener {
            rootView.nameLayout.error = null
            rootView.nameLayout.isErrorEnabled = false
        }

        rootView.chooseLocationButton.setOnClickListener {
            findNavController(rootView)
                .navigate(AlbumNewUpdateFragmentDirections.actionAlbumNewUpdateToLocation())
        }

        rootView.coverImage.setOnClickListener {
            selectCoverImage()
        }

        rootView.radioGroup.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.locationRadioButton -> {
                    rootView.locationLayout.isVisible = true
                    rootView.name.isEnabled = false
                }
                R.id.othersRadioButton -> {
                    rootView.locationLayout.isVisible = false
                    rootView.name.isEnabled = true
                }
            }
        }

        updateAlbumAdapter.applyListener(object : RemovePhotoListener {
            override fun removePhoto(photoId: Long) {
                albumNewUpdateViewModel.removePhotoFromAlbum(photoId, safeArgs.albumId)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_album_new_update, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.tick -> {
                if (!checkValidInput())
                    return true

                if (safeArgs.albumId == -1L) {
                    createNewAlbum()
                } else {
                    updateCurrentAlbum()
                }

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun selectCoverImage() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_pick_photo, null)
        dialogView.photoRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        val adapter = PhotosAdapter()
        dialogView.photoRecyclerView.adapter = adapter
        dialogView.photoRecyclerView.addItemDecoration(GridSpacingItemDecoration(2, 16, false))

        albumNewUpdateViewModel.getAllPhotos()
            .nonNull()
            .observe(viewLifecycleOwner, Observer {
                adapter.addPhotos(it)
                dialogView.noPhotoTextView.isVisible = it.isEmpty()
            })

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.choose_a_cover_image))
            .setView(dialogView)
            .create()

        adapter.applyListener(object : PhotoClickListener {
            override fun onPhotoClicked(photoId: Long) {
                albumNewUpdateViewModel.pickCoverImage(photoId)
                dialog.cancel()
            }
        })

        dialog.show()
    }

    private fun createNewAlbum() {
        var newAlbum = Album(title = rootView.name.text.toString(),
            description = rootView.description.text.toString(),
            type = if (rootView.othersRadioButton.isChecked)
                AlbumConstant.TYPE_OTHERS else AlbumConstant.TYPE_COUNTRY,
            coverImage = albumNewUpdateViewModel.coverImage.value.notNull())

        if (newAlbum.type == AlbumConstant.TYPE_COUNTRY) {
            newAlbum = newAlbum.copy(location = if (rootView.location.text == getString(R.string.no_location_selected))
                "" else rootView.location.text.toString())
        }
        albumNewUpdateViewModel.createNewAlbum(newAlbum)
    }

    private fun updateCurrentAlbum() {
        var updatedAlbum = album.copy(title = rootView.name.text.toString(),
            description = rootView.description.text.toString(),
            type = if (rootView.othersRadioButton.isChecked)
                AlbumConstant.TYPE_OTHERS else AlbumConstant.TYPE_COUNTRY,
            coverImage = albumNewUpdateViewModel.coverImage.value ?: album.coverImage)
        if (updatedAlbum.type == AlbumConstant.TYPE_COUNTRY) {
            updatedAlbum = updatedAlbum.copy(location = if (rootView.location.text == getString(R.string.no_location_selected))
                "" else rootView.location.text.toString())
        }
        albumNewUpdateViewModel.updateAlbum(updatedAlbum)
    }

    private fun checkValidInput(): Boolean {
        var valid = true
        if (rootView.name.text.isNullOrEmpty()) {
            rootView.nameLayout.error = getString(R.string.error_message_empty_album_name)
            valid = false
        }
        if (!rootView.locationRadioButton.isChecked && !rootView.othersRadioButton.isChecked) {
            rootView.locationRadioButton.error = getString(R.string.error_message_no_album_type_selected)
            valid = false
        }
        if (rootView.locationRadioButton.isChecked && rootView.location.text == getString(R.string.no_location_selected)) {
            rootView.snackbar(getString(R.string.message_select_location_for_album))
        }
        return valid
    }
}