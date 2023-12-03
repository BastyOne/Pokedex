
// ClockModule.kt
package com.example.pokedex.util

import java.text.SimpleDateFormat
import java.util.*

object ClockModule {
    fun getCurrentTime(): String {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("America/Santiago")
        return dateFormat.format(Date())
    }
}
