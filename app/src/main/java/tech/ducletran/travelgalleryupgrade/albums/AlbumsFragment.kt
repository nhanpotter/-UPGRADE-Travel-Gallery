package tech.ducletran.travelgalleryupgrade.albums

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_albums.view.othersAlbumRecyclerView
import kotlinx.android.synthetic.main.fragment_albums.view.locationAlbumRecyclerView
import kotlinx.android.synthetic.main.fragment_albums.view.locationAlbums
import kotlinx.android.synthetic.main.fragment_albums.view.othersAlbum
import kotlinx.android.synthetic.main.fragment_albums.view.emptyView
import kotlinx.android.synthetic.main.fragment_albums.view.newAlbum
import kotlinx.android.synthetic.main.fragment_albums.view.foodAlbum
import kotlinx.android.synthetic.main.fragment_albums.view.favoriteAlbum
import kotlinx.android.synthetic.main.fragment_albums.view.friendAlbum
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.ducletran.travelgalleryupgrade.R
import tech.ducletran.travelgalleryupgrade.ext.nonNull
import tech.ducletran.travelgalleryupgrade.ext.snackbar

class AlbumsFragment : Fragment() {

    private lateinit var rootView: View
    private val othersAlbumAdapter = AlbumAdapter()
    private val locationAlbumAdapter = AlbumAdapter()
    private val albumsViewModel by viewModel<AlbumsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_albums, container, false)

        rootView.othersAlbumRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rootView.locationAlbumRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rootView.othersAlbumRecyclerView.adapter = othersAlbumAdapter
        rootView.locationAlbumRecyclerView.adapter = locationAlbumAdapter

        albumsViewModel.getAllAlbums()
            .nonNull()
            .observe(viewLifecycleOwner, Observer {
                val othersAlbum = it.filter { it.type == AlbumConstant.TYPE_OTHERS }
                val locationsAlbum = it.filter { it.type == AlbumConstant.TYPE_COUNTRY }
                othersAlbumAdapter.addAlbums(othersAlbum)
                locationAlbumAdapter.addAlbums(locationsAlbum)
                handleAlbumsVisibility(locationsAlbum, othersAlbum)
            })

        albumsViewModel.message
            .observe(viewLifecycleOwner, Observer {
                rootView.snackbar(it)
            })

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val albumClickListener = object : AlbumClickedListener {
            override fun onAlbumClicked(albumId: Long) {
                findNavController(rootView)
                    .navigate(AlbumsFragmentDirections.actionAlbumsToAlbumDetails(albumId))
            }
        }
        locationAlbumAdapter.applyListener(albumClickListener)
        othersAlbumAdapter.applyListener(albumClickListener)
        rootView.favoriteAlbum.setOnClickListener {
            albumClickListener.onAlbumClicked(AlbumConstant.FAVORITE_ID)
        }
        rootView.foodAlbum.setOnClickListener {
            albumClickListener.onAlbumClicked(AlbumConstant.FOOD_ID)
        }
        rootView.friendAlbum.setOnClickListener {
            albumClickListener.onAlbumClicked(AlbumConstant.PEOPLE_ID)
        }
        rootView.newAlbum.setOnClickListener {
            createNewAlbum()
        }
    }

    private fun handleAlbumsVisibility(locationAlbums: List<Album>, othersAlbum: List<Album>) {
        rootView.locationAlbums.isVisible = locationAlbums.isNotEmpty()
        rootView.othersAlbum.isVisible = othersAlbum.isNotEmpty()
        rootView.emptyView.isVisible = locationAlbums.isEmpty() && othersAlbum.isEmpty()
    }

    private fun createNewAlbum() {
        findNavController(rootView).navigate(AlbumsFragmentDirections.actionAlbumsToAlbumNew())
    }
}