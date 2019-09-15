package tech.ducletran.travelgalleryupgrade.photodetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_photo_info.view.previewMap
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.ducletran.travelgalleryupgrade.R
import tech.ducletran.travelgalleryupgrade.databinding.FragmentPhotoInfoBinding
import tech.ducletran.travelgalleryupgrade.ext.nonNull
import tech.ducletran.travelgalleryupgrade.photos.Photo

class PhotoInfoFragment : Fragment() {

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_photo_info, container, false)
        rootView = binding.root

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
    }
}