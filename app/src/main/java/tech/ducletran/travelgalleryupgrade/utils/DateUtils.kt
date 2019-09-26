package tech.ducletran.travelgalleryupgrade.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {

    const val FORMAT_TIME = "HH:mm:ss"
    const val FORMAT_DATE_SIMPLE = "MMM dd, EEE yyyy"
    const val FORMAT_DATE_DETAILS = "EEE, dd MMM yyyy 'at' HH:mm:ss" // To store info in the database
    const val FORMAT_DATE_FROM_FILE = "yyyy:MM:dd HH:mm:ss"

    fun convertDateToString(date: Date, format: String): String {
        val formatter = SimpleDateFormat(format, Locale.US)
        return formatter.format(date)
    }

    fun convertStringToDate(date: String, format: String): Date =
            SimpleDateFormat(format, Locale.US).parse(date)!!

    fun getCurrentDateString(format: String) = convertDateToString(Date(), format)

    fun convertDateBetweenFormats(date: String, firstFormat: String, secondFormat: String): String {
        return SimpleDateFormat(secondFormat, Locale.US).format(convertStringToDate(date, firstFormat))
    }
}