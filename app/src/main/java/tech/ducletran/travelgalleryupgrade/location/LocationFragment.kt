package tech.ducletran.travelgalleryupgrade.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Menu
import android.view.MenuInflater
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_location.view.locationsRecyclerView
import kotlinx.android.synthetic.main.fragment_location.view.progress
import kotlinx.android.synthetic.main.fragment_location.view.emptyView
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.ducletran.travelgalleryupgrade.R
import tech.ducletran.travelgalleryupgrade.ext.nonNull

const val LOCATION = "location"

class LocationFragment : Fragment() {

    private val locationViewModel by viewModel<LocationViewModel>()
    private lateinit var rootView: View
    private val adapter = LocationAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        rootView = inflater.inflate(R.layout.fragment_location, container, false)
        rootView.progress.isIndeterminate = true

        rootView.locationsRecyclerView.adapter = adapter
        rootView.locationsRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        locationViewModel.loading
            .nonNull()
            .observe(viewLifecycleOwner, Observer {
                rootView.progress.isVisible = it
            })

        locationViewModel.carmenFeatures
            .nonNull()
            .observe(viewLifecycleOwner, Observer {
                adapter.submitList(it)
                rootView.emptyView.isVisible = it.isEmpty()
            })

        locationViewModel.carmenFeatures
            .value.let {
            rootView.emptyView.isVisible = it.isNullOrEmpty()
            adapter.submitList(it)
        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.applyListener(object : LocationClickListener {
            override fun onLocationSelected(location: String) {
                locationViewModel.selectLocation(location)
                findNavController(rootView).popBackStack()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_location, menu)

        val searchItem = menu.findItem(R.id.actionSearch)
        val searchView = MenuItemCompat.getActionView(searchItem)

        if (searchView is SearchView) {
            searchView.queryHint = getString(R.string.search_location)
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean = true

                override fun onQueryTextChange(newText: String?): Boolean {
                    locationViewModel.loadPlaceSuggestions(newText)
                    return true
                }
            })
        }
    }
}