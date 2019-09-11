package tech.ducletran.travelgalleryupgrade.photos

import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_photos.view.emptyView
import kotlinx.android.synthetic.main.fragment_photos.view.photoGrids
import kotlinx.android.synthetic.main.fragment_photos.view.loadingLayout
import kotlinx.android.synthetic.main.fragment_photos.view.loadPhotosButton
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.ducletran.travelgalleryupgrade.R
import tech.ducletran.travelgalleryupgrade.ext.nonNull
import tech.ducletran.travelgalleryupgrade.ext.notNull
import tech.ducletran.travelgalleryupgrade.utils.Utils.getLatitude
import tech.ducletran.travelgalleryupgrade.utils.Utils.getLongitude

class PhotosFragment: Fragment() {

    companion object {
        private const val PICK_PHOTO_REQUEST_CODE = 1
    }

    private val photosAdapter = PhotosAdapter()
    private lateinit var rootView: View
    private val photosViewModel by viewModel<PhotosViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_photos, container, false)

        val layoutManager = GridLayoutManager(requireContext(),4,GridLayoutManager.VERTICAL, false)
        rootView.photoGrids.layoutManager = layoutManager
        rootView.photoGrids.adapter = photosAdapter

        photosViewModel.photos
            .nonNull()
            .observe(viewLifecycleOwner, Observer {
                photosAdapter.addPhotos(it)
                rootView.emptyView.isVisible = it.isEmpty()
            })

        photosViewModel.loading
            .nonNull()
            .observe(viewLifecycleOwner, Observer {
                showLoadingDialog(it)
            })

        val currentPhotos = photosViewModel.photos.value
        if (currentPhotos == null)
            photosViewModel.loadAllPhotos()
        else {
            photosAdapter.addPhotos(currentPhotos)
            rootView.emptyView.isVisible = currentPhotos.isEmpty()
        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView.loadPhotosButton.setOnClickListener {
            importPhotosFromGallery()
        }

        photosAdapter.applyListener(object: PhotoClickListener {
            override fun onPhotoClicked(photoId: Long) {
                Navigation.findNavController(rootView)
                    .navigate(PhotosFragmentDirections.actionPhotosToPhotoDetail(photoId))
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_PHOTO_REQUEST_CODE) {
            if (data != null) {
                data.data?.let {
                    handleOnePhotoPicked(it)
                } ?: data.clipData?.let {
                    handleMultiplePhotosPicked(it)
                }
            }
        }
    }

    private fun handleOnePhotoPicked(photoUri: Uri) {
        val contentResolver = activity?.contentResolver ?: return
        val cursor = contentResolver.query(photoUri, null, null, null, null) ?: return
        cursor.moveToFirst()

        val title = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        val size = cursor.getString(cursor.getColumnIndex(OpenableColumns.SIZE))
        var photo = Photo(url = photoUri.toString(), title = title, size = size)
        contentResolver.openInputStream(photoUri)?.let {stream ->
            ExifInterface(stream).let {
                photo = getPhotoWithMetadata(photo, it)
            }
        }
        photosViewModel.addPhotos(mutableListOf(photo))

        cursor.close()
    }

    private fun handleMultiplePhotosPicked(clipData: ClipData) {
        val newPhotos = mutableListOf<Photo>()

        for (index in 0 until clipData.itemCount) {
            val photoUri = clipData.getItemAt(index).uri
            val contentResolver = activity?.contentResolver ?: return
            val cursor = contentResolver.query(photoUri, null, null, null, null) ?: continue
            cursor.moveToFirst()

            val title = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            val size = cursor.getString(cursor.getColumnIndex(OpenableColumns.SIZE))
            var photo = Photo(url = photoUri.toString(), title = title, size = size)
            contentResolver.openInputStream(photoUri)?.let {stream ->
                ExifInterface(stream).let {
                    photo = getPhotoWithMetadata(photo, it)
                }
            }
            newPhotos.add(photo)
            cursor.close()
        }
        photosViewModel.addPhotos(newPhotos)
    }

    private fun getPhotoWithMetadata(photo: Photo, exif: ExifInterface): Photo {
        val latitude = getLatitude(exif)
        val longitude = getLongitude(exif)
        return photo.copy(
            latitude = latitude,
            longitude = longitude,
            description = exif.getAttribute(ExifInterface.TAG_IMAGE_DESCRIPTION).notNull(),
            width = exif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH).notNull(),
            height = exif.getAttribute(ExifInterface.TAG_IMAGE_LENGTH).notNull(),
            dateTaken = exif.getAttribute(ExifInterface.TAG_DATETIME).notNull()
        )
    }

    private fun importPhotosFromGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_OPEN_DOCUMENT
        startActivityForResult(
            Intent.createChooser(intent, "Select Photos"), PICK_PHOTO_REQUEST_CODE)
    }

    private fun showLoadingDialog(show: Boolean) {
        rootView.loadingLayout.isVisible = show
        rootView.loadPhotosButton.isVisible = !show
    }
}