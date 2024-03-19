package com.example.educarecalendar

import android.content.Context
import android.graphics.Color
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.time.LocalDate
import java.time.Year
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


data class Events(
    val calendarName: String,
    val calendarDesc1: String,
    val calendarText: String,
    val dateList: List<LocalDate>,
    val color: Int
)
var eventArrayEvents = ArrayList<Events>()
fun requestEvents(context: Context){
    val requestQueue = Volley.newRequestQueue(context)
    val url = "https://api.myeducare.ro/parent_api/parent_get_school_events.php"
    val params = HashMap<String, String>()
    params["school_group"] = "educare"
    params["db"] = "educare_2023_2024"
    params["student_idx"] = "8443"
    //val eventArray = ArrayList<Events>()

    val stringRequest = object : StringRequest(
        Request.Method.POST, url,
        Response.Listener { response ->
            try {
                val jsonObject = JSONObject(response)
                val jsonArray : JSONArray = jsonObject.getJSONArray("data")


                for (i in 0 until jsonArray.length()) {
                    val jsonObject1 = jsonArray.getJSONObject(i)
                    val name = jsonObject1.getString("name")
                    val desc1 = jsonObject1.getString("desc1")
                    val text = jsonObject1.getString("text")
                    val startDate = toDate(jsonObject1.getString("start_date"))
                    val endDate = toDate(jsonObject1.getString("end_date"))
                    val datesArray = generateDates(startDate, endDate)
                    val event = Events(name,desc1,text,datesArray, Color.BLUE)
                    eventArrayEvents.add(event)


                }

            } catch (e: JSONException) {
                e.printStackTrace()
            }
        },
        Response.ErrorListener { error ->
            Log.e("ERROR", error.toString())
        }) {
        override fun getParams(): Map<String, String> {
            return params
        }
    }
    requestQueue.add(stringRequest)
}

fun toDate(date: String): LocalDate {
    //By utilizing DateTimeFormatter this is used to convert the string date to LocalDate type
    val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
    return LocalDate.parse(date, formatter)
}

fun generateDates(startDate:LocalDate, endDate:LocalDate): List<LocalDate>{
    val numOfDaysBetween = ChronoUnit.DAYS.between(startDate,endDate).toInt()
    return generateSequence(startDate) { it.plusDays(1) }
        .take(numOfDaysBetween + 1)
        .toList()
}

fun dupeDays(eventArray: ArrayList<Events>): HashMap<LocalDate, Int>{
    // this function is used to count duplicate event dates and assigns a number depending on the
    //count utilizing hashmap
    val eventDates = oneDateArrayToRuleThemAll(eventArray)
    val hashDate = HashMap<LocalDate,Int>()
    var i = 0
    for (event in eventDates){
        var detected = false
        for (temp in hashDate.keys){
            if (compareDates(temp,event)){
                hashDate[temp] = hashDate[temp]!!+1
                detected = true
                break
            }
        }
        if (!detected){
            hashDate[event] = 1
        }
    }
    return hashDate
}

private fun compareDates(date1:LocalDate, date2: LocalDate): Boolean {
    return date1==date2
}

private fun oneDateArrayToRuleThemAll(eventArray:ArrayList<Events>): ArrayList<LocalDate> {
    val eventDates = ArrayList<LocalDate>()
    for(event in eventArray){
        for (event2 in event.dateList){
            eventDates.add(event2)
        }
    }
    return eventDates
}

fun checkEventAmountPerDay(
    hashDate: HashMap<LocalDate, Int>,
    year: Int,
    month: Int,
    day: Int): Int{
    val date = LocalDate.of(year, month, day)
    return if (hashDate[date]==null) 0
    else hashDate[date]!!

}