package tech.ducletran.travelgalleryupgrade.photos.picker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_photos_picker.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.ducletran.travelgalleryupgrade.R
import tech.ducletran.travelgalleryupgrade.customclass.GridSpacingItemDecoration
import tech.ducletran.travelgalleryupgrade.ext.nonNull
import tech.ducletran.travelgalleryupgrade.ext.snackbar
import tech.ducletran.travelgalleryupgrade.photos.Photo

class PhotosPickerFragment : Fragment() {

    private val photosPickerViewModel by viewModel<PickPhotosViewModel>()
    private lateinit var rootView: View
    private lateinit var tickMenuItem: MenuItem
    private val adapter = PhotoPickerAdapter()
    private val safeArgs: PhotosPickerFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        rootView = layoutInflater.inflate(R.layout.fragment_photos_picker, container, false)

        rootView.photoGrids.layoutManager = GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
        rootView.photoGrids.adapter = adapter
        rootView.photoGrids.addItemDecoration(GridSpacingItemDecoration(3, 16, true))

        photosPickerViewModel.getAllPhotos()
            .nonNull()
            .observe(viewLifecycleOwner, Observer {
                adapter.updateList(it.map { photo ->
                    Pair(photosPickerViewModel.isSelected(photo.id), photo) })
                rootView.emptyView.isVisible = it.isEmpty()
            })

        photosPickerViewModel.message
            .observe(viewLifecycleOwner, Observer {
                rootView.snackbar(it)
            })

        photosPickerViewModel.selectDone
            .nonNull()
            .observe(viewLifecycleOwner, Observer {
                findNavController(rootView).popBackStack()
            })

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.applyListener(object : PhotoSelectListener {
            override fun select(photo: Photo, isSelected: Boolean, position: Int) {
                photosPickerViewModel.selectPhoto(photo.id, !isSelected)
                adapter.updatePhoto(position, !isSelected)
                tickMenuItem.isVisible = photosPickerViewModel.getNumsOfPhotosSelected() > 0
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_photos_picker, menu)
        tickMenuItem = menu.findItem(R.id.actionSelectPhotos)
        tickMenuItem.isVisible = photosPickerViewModel.getNumsOfPhotosSelected() > 0
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.actionSelectPhotos -> {
                photosPickerViewModel.addPhotosToAlbum(safeArgs.albumId)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}