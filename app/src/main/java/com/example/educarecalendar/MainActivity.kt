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
            var visible = remember { mutableStateOf(true) }
            var monthName = remember { mutableStateOf(calendarSetter("month")) }
            var year = remember { mutableStateOf(calendarSetter("year")) }
            requestEvents(this)

            Log.d("DEBUG","size in main ${eventArray.value.size}")
            wait.value = eventArray.value.size!=0
            if (wait.value) {
                CalendarParent(
                    monthName = calendarSetter("month"),
                    year = calendarSetter("year"),
                    eventArray = eventArray.value
                )
            }
            MonthPicker(
                visible = visible.value,
                monthNum = calendarSetter("monthNum").toInt(),
                year = calendarSetter("year").toInt(),
                confirmButtonClicked = {month_, year_ ->
                    monthName.value = monthNameReturner(month_)
                    year.value = year_.toString()
                    visible.value = false
                    eventArray.value = eventArrayEvents
                },
                cancelButtonClicked = {
                    visible.value = false
                }
            )
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
            "monthNum" -> {LocalDate.now().monthValue.toString()}
            else -> "Placeholder"
        }
    }

}

