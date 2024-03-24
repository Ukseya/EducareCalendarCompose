package com.example.educarecalendar

import java.time.LocalDate

fun getDay(day: LocalDate):ArrayList<Events>{
    //depending on the given LocalDate fills an ArrayList and returns it
    //with the selected day's events
    val arrayTemp = ArrayList<Events>()
    for (event in eventArrayEvents){
        for (date in event.dateList){
            if(day == date){
                arrayTemp.add(event)
            }
        }
    }
    return arrayTemp
}

fun calBreaker(absFirstDay: Int, firstDay: Int, i: Int): Boolean {
    //calBreaker function determines when the week ends
    //for example if a month starts on a wednesday the first week row should end at
    //day number 5
    when(absFirstDay){
        -1 -> if(firstDay==(7*i)-1)return false
        -2 -> if(firstDay==(7*i)-2)return false
        -3 -> if(firstDay==(7*i)-3)return false
        -4 -> if(firstDay==(7*i)-4)return false
        -5 -> if(firstDay==(7*i)-5)return false
        -6 -> if(firstDay==(7*i)-6)return false
    }
    return true
}

fun monthNumReturner(monthName: String): Int{
    //returns the number of the given month name
    return when (monthName) {
        "January" -> 1
        "February" -> 2
        "March" -> 3
        "April" -> 4
        "May" -> 5
        "June" -> 6
        "July" -> 7
        "August" -> 8
        "September" -> 9
        "October" -> 10
        "November" -> 11
        "December" -> 12
        else -> throw IllegalArgumentException("Invalid month name: $monthName")
    }
}

fun monthNameReturner(monthNum: Int):String{
    //returns the name of the given month number
    return when (monthNum) {
        1 -> "January"
        2 -> "February"
        3 -> "March"
        4 -> "April"
        5 -> "May"
        6 -> "June"
        7 -> "July"
        8 -> "August"
        9 -> "September"
        10 -> "October"
        11 -> "November"
        12 -> "December"
        else -> throw IllegalArgumentException("Invalid month name: $monthNum")
    }
}

fun amountODaysReturner(monthNum: Int, year: Int):Int{
    //returns how many days depending on the month number value
    return when(monthNum){
        1,3,5,7,8,10,12 -> 31
        4,6,9,11 -> 30
        2-> if (year%4==0){29}else 28
        else -> {throw IllegalArgumentException("Invalid month number: $monthNum")}
    }
}

fun fDayNumReturner(dayName:String):Int{
    //used to return the number of the given day
    return when(dayName){
        "MONDAY" -> 1
        "TUESDAY" -> 2
        "WEDNESDAY" -> 3
        "THURSDAY" -> 4
        "FRIDAY" -> 5
        "SATURDAY" -> 6
        "SUNDAY" -> 7
        else -> {throw IllegalArgumentException("Invalid month name: $dayName")}
    }
}