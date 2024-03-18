package com.example.educarecalendar

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import java.time.LocalDate
import java.util.Locale

class MainActivity : ComponentActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val handler = Handler()
        requestEvents(this)
        handler.postDelayed({
            val eventArray = eventArrayEvents
            Log.d("eventArraySizeInMain","${eventArray.size}")



        setContent {

            CalendarParent(
                monthName = calendarSetter("month"),
                year = calendarSetter("year"),
                eventArray = eventArray
                )
            }
        },5000)

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

