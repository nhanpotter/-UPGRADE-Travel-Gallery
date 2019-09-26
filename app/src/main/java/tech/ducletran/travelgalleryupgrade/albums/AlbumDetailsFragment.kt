package tech.ducletran.travelgalleryupgrade.albums

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.MenuItem
import android.view.Menu
import android.view.MenuInflater
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_album_details.view.photoGrids
import kotlinx.android.synthetic.main.fragment_album_details.view.emptyView
import kotlinx.android.synthetic.main.fragment_album_details.view.progress
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.ducletran.travelgalleryupgrade.R
import tech.ducletran.travelgalleryupgrade.databinding.FragmentAlbumDetailsBinding
import tech.ducletran.travelgalleryupgrade.ext.nonNull
import tech.ducletran.travelgalleryupgrade.ext.snackbar
import tech.ducletran.travelgalleryupgrade.photos.PhotoClickListener
import tech.ducletran.travelgalleryupgrade.photos.PhotosAdapter

class AlbumDetailsFragment : Fragment() {

    private lateinit var rootView: View
    private val safeArgs: AlbumDetailsFragmentArgs by navArgs()
    private val albumsViewModel by viewModel<AlbumsViewModel>()
    private val photosAdapter = PhotosAdapter()
    private lateinit var binding: FragmentAlbumDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_album_details, container, false)
        rootView = binding.root

        rootView.photoGrids.layoutManager = GridLayoutManager(requireContext(), 4, GridLayoutManager.VERTICAL, false)
        rootView.photoGrids.adapter = photosAdapter

        albumsViewModel.getPhotosFromAlbum(safeArgs.albumId)
            .nonNull()
            .observe(viewLifecycleOwner, Observer {
                photosAdapter.addPhotos(it)
                rootView.emptyView.isVisible = it.isEmpty()
            })

        albumsViewModel.progress
            .nonNull()
            .observe(viewLifecycleOwner, Observer {
                rootView.progress.isVisible = it
            })

        albumsViewModel.message
            .observe(viewLifecycleOwner, Observer {
                rootView.snackbar(it)
            })

        setupAlbum()

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        photosAdapter.applyListener(object : PhotoClickListener {
            override fun onPhotoClicked(photoId: Long) {
                findNavController(rootView)
                    .navigate(AlbumDetailsFragmentDirections.actionAlbumDetailsToPhotoDetails(photoId))
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_album_details, menu)
        if (safeArgs.albumId < 0) { // Special Type Album (Favorite, Food, People)
            menu.findItem(R.id.actionDeleteAlbum).isVisible = false
            menu.findItem(R.id.actionEditAlbum).isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.actionAddPhotos -> {
                findNavController(rootView).navigate(AlbumDetailsFragmentDirections
                    .actionAlbumDetailsToPhotoPicker(safeArgs.albumId))
                true
            }
            R.id.actionDeleteAlbum -> {
                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.delete_this_album))
                    .setMessage(getString(R.string.message_delete_album))
                    .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                        dialog.cancel()
                    }.setPositiveButton(getString(R.string.continue_string)) { _, _ ->
                        deleteAlbum()
                    }.create()
                    .show()

                true
            }
            R.id.actionEditAlbum -> {
                editAlbum()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupAlbum() {
        when (safeArgs.albumId) {
            AlbumConstant.FAVORITE_ID -> {
                binding.album = Album(title = getString(R.string.favorite), description = getString(R.string.description_favorite_album))
                binding.executePendingBindings()
            }
            AlbumConstant.FOOD_ID -> {
                binding.album = Album(title = getString(R.string.food), description = getString(R.string.description_food_album))
                binding.executePendingBindings()
            }
            AlbumConstant.PEOPLE_ID -> {
                binding.album = Album(title = getString(R.string.people), description = getString(R.string.description_people_album))
                binding.executePendingBindings()
            }
            else -> albumsViewModel.getAlbumById(safeArgs.albumId)
                .nonNull()
                .observe(viewLifecycleOwner, Observer {
                    binding.album = it
                    binding.executePendingBindings()
                })
        }
    }

    private fun editAlbum() {
        findNavController(rootView).navigate(AlbumDetailsFragmentDirections
            .actionAlbumDetailsToAlbumUpdate(safeArgs.albumId))
    }

    private fun deleteAlbum() {
        albumsViewModel.albumDeleted
            .nonNull()
            .observe(viewLifecycleOwner, Observer {
                if (it) activity?.onBackPressed()
            })

        albumsViewModel.removeAlbum(safeArgs.albumId)
    }
}