package tech.ducletran.travelgalleryupgrade.utils

import android.media.ExifInterface

object Utils {

    fun getLatitude(exif: ExifInterface): String {
        val lat = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE)
        val latRef = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF)
        if (lat == null || latRef == null) return ""

        val latitudeResult = if (latRef == "N")
            convertToLocationToDoubleValue(lat) else 0 - convertToLocationToDoubleValue(lat)

        return latitudeResult.toString()
    }

    fun getLongitude(exif: ExifInterface): String {
        val lng = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)
        val lngRef = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF)
        if (lng == null || lngRef == null) return ""

        val longitudeResult = if (lngRef == "E")
            convertToLocationToDoubleValue(lng) else 0 - convertToLocationToDoubleValue(lng)

        return longitudeResult.toString()
    }

    private fun convertToLocationToDoubleValue(dmsString: String): Double {
        val dms = dmsString.split(",")

        val stringD = dms[0].split("/")
        val dValue = stringD[0].toDouble() / stringD[1].toDouble()

        val stringM = dms[1].split("/")
        val mValue = stringM[0].toDouble() / stringM[1].toDouble()

        val stringS = dms[2].split("/")
        val sValue = stringS[0].toDouble() / stringS[1].toDouble()

        return dValue + mValue / 60 + sValue / 3600
    }
}
