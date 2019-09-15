package tech.ducletran.travelgalleryupgrade.albums

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_album_details.view.photoGrids
import kotlinx.android.synthetic.main.fragment_album_details.view.emptyView
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.ducletran.travelgalleryupgrade.R
import tech.ducletran.travelgalleryupgrade.ext.nonNull
import tech.ducletran.travelgalleryupgrade.photos.PhotoClickListener
import tech.ducletran.travelgalleryupgrade.photos.PhotosAdapter

class AlbumDetailsFragment : Fragment() {

    private lateinit var rootView: View
    private val safeArgs: AlbumDetailsFragmentArgs by navArgs()
    private val albumsViewModel by viewModel<AlbumsViewModel>()
    private val photosAdapter = PhotosAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_album_details, container, false)

        rootView.photoGrids.layoutManager = GridLayoutManager(requireContext(), 4, GridLayoutManager.VERTICAL, false)
        rootView.photoGrids.adapter = photosAdapter

        albumsViewModel.getPhotosFromAlbum(safeArgs.albumId)
            .nonNull()
            .observe(viewLifecycleOwner, Observer {
                photosAdapter.addPhotos(it)
                rootView.emptyView.isVisible = it.isEmpty()
            })

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
}