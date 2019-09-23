package tech.ducletran.travelgalleryupgrade.albums

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
import kotlinx.android.synthetic.main.fragment_album_new_update.view.title
import kotlinx.android.synthetic.main.fragment_album_new_update.view.locationRadioButton
import kotlinx.android.synthetic.main.fragment_album_new_update.view.othersRadioButton
import kotlinx.android.synthetic.main.fragment_album_new_update.view.radioGroup
import kotlinx.android.synthetic.main.fragment_album_new_update.view.name
import kotlinx.android.synthetic.main.fragment_album_new_update.view.nameLayout
import kotlinx.android.synthetic.main.fragment_album_new_update.view.description
import kotlinx.android.synthetic.main.fragment_album_new_update.view.location
import kotlinx.android.synthetic.main.fragment_album_new_update.view.locationLayout
import kotlinx.android.synthetic.main.fragment_album_new_update.view.chooseLocationButton
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.ducletran.travelgalleryupgrade.R
import tech.ducletran.travelgalleryupgrade.ext.hideKeyboard
import tech.ducletran.travelgalleryupgrade.ext.nonNull
import tech.ducletran.travelgalleryupgrade.ext.snackbar

class AlbumNewUpdateFragment : Fragment() {

    private lateinit var rootView: View
    private val safeArgs: AlbumNewUpdateFragmentArgs by navArgs()
    private val albumsViewModel by viewModel<AlbumsViewModel>()
    private lateinit var album: Album

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        rootView = inflater.inflate(R.layout.fragment_album_new_update, container, false)

        albumsViewModel.message
            .observe(viewLifecycleOwner, Observer {
                rootView.snackbar(it)
            })

        albumsViewModel.albumCreatedOrUpdated
            .nonNull()
            .observe(viewLifecycleOwner, Observer {
                rootView.hideKeyboard(activity)
                findNavController(rootView).popBackStack()
            })

        if (safeArgs.albumId != -1L) {
            rootView.title.text = getString(R.string.updating_album)
            albumsViewModel.getAlbumById(safeArgs.albumId)
                .nonNull()
                .observe(viewLifecycleOwner, Observer {
                    album = it
                    rootView.name.setText(it.title)
                    rootView.description.setText(it.description)

                    if (rootView.location.text == getString(R.string.no_location_selected)) {
                        rootView.location.text = it.location
                    }
                    rootView.radioGroup.check(if (it.type == AlbumConstant.TYPE_OTHERS)
                        R.id.othersRadioButton else R.id.locationRadioButton)
                })
        }

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

        rootView.radioGroup.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.locationRadioButton -> {
                    rootView.locationLayout.isVisible = true
                }
                R.id.othersRadioButton -> {
                    rootView.locationLayout.isVisible = false
                }
            }
        }
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

    private fun createNewAlbum() {
        var newAlbum = Album(title = rootView.name.text.toString(),
            description = rootView.description.text.toString(),
            type = if (rootView.othersRadioButton.isChecked)
                AlbumConstant.TYPE_OTHERS else AlbumConstant.TYPE_COUNTRY)

        if (newAlbum.type == AlbumConstant.TYPE_COUNTRY) {
            newAlbum = newAlbum.copy(location = if (rootView.location.text == getString(R.string.no_location_selected))
                "" else rootView.location.text.toString())
        }
        albumsViewModel.createNewAlbum(newAlbum)
    }

    private fun updateCurrentAlbum() {
        var updatedAlbum = album.copy(title = rootView.name.text.toString(),
            description = rootView.description.text.toString(),
            type = if (rootView.othersRadioButton.isChecked)
                AlbumConstant.TYPE_OTHERS else AlbumConstant.TYPE_COUNTRY)
        if (updatedAlbum.type == AlbumConstant.TYPE_COUNTRY) {
            updatedAlbum = updatedAlbum.copy(location = if (rootView.location.text == getString(R.string.no_location_selected))
                "" else rootView.location.text.toString())
        }
        albumsViewModel.updateAlbum(updatedAlbum)
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
        return valid
    }
}