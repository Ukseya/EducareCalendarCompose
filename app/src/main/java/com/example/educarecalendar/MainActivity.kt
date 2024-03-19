package com.example.educarecalendar

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import java.time.LocalDate
import java.util.Locale

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var wait = remember { mutableStateOf(false) }
            var eventArray = remember { mutableStateOf(eventArrayEvents) }
            requestEvents(this)
            eventArray.value = eventArrayEvents
            Log.d("eventArraySizeInMain","${eventArray.value.size}")
            wait.value = eventArray.value.size!=0
            if (wait.value) {
                CalendarParent(
                    monthName = calendarSetter("month"),
                    year = calendarSetter("year"),
                    eventArray = eventArray.value
                )
            }
        }
    }
    private fun calendarSetter(whtDYouWant: String): String{
        return when (whtDYouWant) {
            "year" -> {
                LocalDate.now().year.toString()
            }
            "month" -> {
                LocalDate.now().month
                    .toString()
                    .lowercase(Locale.getDefault())
                    .capitalize()
            }
            else -> "Placeholder"
        }
    }

}

