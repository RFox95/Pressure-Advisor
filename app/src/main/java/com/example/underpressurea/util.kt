package com.example.underpressurea

import java.text.SimpleDateFormat
import java.util.*

fun convertTimeToFormatted(startTimeMilli: Long): String {
    return SimpleDateFormat("EEE, MMM d, ''yy", Locale.ENGLISH).format(startTimeMilli)

}


