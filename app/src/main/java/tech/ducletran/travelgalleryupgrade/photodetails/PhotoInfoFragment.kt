package tech.ducletran.travelgalleryupgrade.photodetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.dialog_edit_photo_info.view.description
import kotlinx.android.synthetic.main.dialog_edit_photo_info.view.datePicker
import kotlinx.android.synthetic.main.fragment_photo_info.view.chooseLocationButton
import kotlinx.android.synthetic.main.fragment_photo_info.view.previewMap
import kotlinx.android.synthetic.main.fragment_photo_info.view.progress
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.ducletran.travelgalleryupgrade.R
import tech.ducletran.travelgalleryupgrade.databinding.FragmentPhotoInfoBinding
import tech.ducletran.travelgalleryupgrade.ext.nonNull
import tech.ducletran.travelgalleryupgrade.ext.snackbar
import tech.ducletran.travelgalleryupgrade.photos.Photo
import tech.ducletran.travelgalleryupgrade.utils.DateUtils
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete
import androidx.core.content.ContextCompat
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions
import tech.ducletran.travelgalleryupgrade.BuildConfig
import android.app.Activity
import androidx.appcompat.app.AlertDialog
import java.util.*

class PhotoInfoFragment : Fragment() {

    private val CHOOSE_LOCATION_REQUEST_CODE = 1

    private val photoDetailsViewModel by viewModel<PhotoDetailsViewModel>()
    private lateinit var rootView: View
    private lateinit var binding: FragmentPhotoInfoBinding
    private val safeArgs: PhotoInfoFragmentArgs by navArgs()
    private lateinit var photo: Photo

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_photo_info, container, false)
        rootView = binding.root
        rootView.progress.isIndeterminate = true

        photoDetailsViewModel.message
            .observe(viewLifecycleOwner, Observer {
                rootView.snackbar(it)
            })

        photoDetailsViewModel.loading
            .nonNull()
            .observe(viewLifecycleOwner, Observer {
                rootView.progress.isVisible = it
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

        rootView.previewMap.setOnClickListener {
            val mapUrl = "geo:<${photo.latitude}>,<${photo.longitude}>" +
                    "?q=<${photo.latitude}>,<${photo.longitude}>"
            val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(mapUrl))
            startActivity(mapIntent)
        }

        rootView.chooseLocationButton.setOnClickListener {
            val intent = PlaceAutocomplete.IntentBuilder()
                .accessToken(BuildConfig.MAPBOX_KEY)
                .placeOptions(PlaceOptions.builder()
                    .language("en")
                    .toolbarColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                    .hint(getString(R.string.searching_for_location))
                    .build()
                )
                .build(requireActivity())
            rootView.isVisible = false
            startActivityForResult(intent, CHOOSE_LOCATION_REQUEST_CODE)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_photo_info, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.actionEditPhoto -> {
                editCurrentPhoto(photo)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        rootView.isVisible = true
        if (resultCode == Activity.RESULT_OK && requestCode == CHOOSE_LOCATION_REQUEST_CODE) {
            val feature = PlaceAutocomplete.getPlace(data)
            photo.latitude = (feature.center()?.latitude()?: "").toString()
            photo.longitude = (feature.center()?.longitude()?: "").toString()
            feature.placeName()?.split(",")?.let {
                photo.placeName = it.first()
            }
            photoDetailsViewModel.updatePhoto(photo)
        }
    }

    private fun editCurrentPhoto(photo: Photo) {
        val view = layoutInflater.inflate(R.layout.dialog_edit_photo_info,null)

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.update_photo_info))
            .setView(view)
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.cancel()
            }.setPositiveButton(getString(R.string.save)) { dialog, _ ->
                photo.description = view.description.text.toString()
                val calendar = Calendar.getInstance()
                calendar.set(view.datePicker.year, view.datePicker.month, view.datePicker.dayOfMonth)
                photo.dateTaken = DateUtils.convertDateToString(calendar.time, DateUtils.FORMAT_DATE_DETAILS)
                photoDetailsViewModel.updatePhoto(photo)
                dialog.cancel()
            }
            .create()
            .show()

        view.description.setText(photo.description)
        val currentDate = if (photo.dateTaken.isEmpty()) Date()
        else DateUtils.convertStringToDate(photo.dateTaken, DateUtils.FORMAT_DATE_DETAILS)
        view.datePicker.updateDate(currentDate.year, currentDate.month, currentDate.day)
    }
}