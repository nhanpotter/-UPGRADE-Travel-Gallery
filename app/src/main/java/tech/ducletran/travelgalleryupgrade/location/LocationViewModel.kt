package tech.ducletran.travelgalleryupgrade.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mapbox.api.geocoding.v5.MapboxGeocoding
import com.mapbox.api.geocoding.v5.models.CarmenFeature
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import tech.ducletran.travelgalleryupgrade.BuildConfig
import tech.ducletran.travelgalleryupgrade.customclass.Preference

class LocationViewModel(
    private val preference: Preference
) : ViewModel() {

    private var geoCodingRequest: MapboxGeocoding? = null
    private val _carmenFeatures = MutableLiveData<List<CarmenFeature>>()
    val carmenFeatures: LiveData<List<CarmenFeature>> = _carmenFeatures
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun selectLocation(location: String) {
        preference.putString(LOCATION, location)
    }

    fun loadPlaceSuggestions(query: String?) {
        geoCodingRequest?.cancelCall()

        if (query.isNullOrEmpty()) {
            _loading.value = false
            _carmenFeatures.value = emptyList()
            return
        }

        _loading.value = true
        geoCodingRequest = MapboxGeocoding.builder()
            .accessToken(BuildConfig.MAPBOX_KEY)
            .query(query)
            .languages("en")
            .build()

        doAsync {
            val response = geoCodingRequest?.executeCall()
            uiThread {
                _carmenFeatures.value = response?.body()?.features()
                _loading.value = false
            }
        }
    }
}