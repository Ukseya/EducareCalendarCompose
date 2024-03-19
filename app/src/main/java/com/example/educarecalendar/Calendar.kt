package com.example.educarecalendar

import android.os.Handler
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.educarecalendar.ui.theme.Col1
import com.example.educarecalendar.ui.theme.Col2
import com.example.educarecalendar.ui.theme.Col3
import com.example.educarecalendar.ui.theme.Col4
import com.example.educarecalendar.ui.theme.Col5
import java.time.LocalDate


@Composable
fun CalendarParent(
    monthName: String,
    year: String,
    eventArray: ArrayList<Events>,
) {
    val colorArray: Array<Color> = arrayOf(Col1,Col2,Col3,Col4,Col5)
    val width = (LocalConfiguration.current.screenWidthDp - 25)/7
    var yearInternal = remember { mutableIntStateOf(year.toInt()) }
    var monthNum = remember {mutableIntStateOf(monthNumReturner(monthName))}
    val todayMonthFDay = remember {
        mutableStateOf(LocalDate.of(
        yearInternal.intValue,
        monthNum.intValue,
        1).dayOfWeek.toString())}
    var todayMonthFDayNum = remember { mutableIntStateOf(fDayNumReturner(todayMonthFDay.value))}
    var amountOfDays = remember { mutableIntStateOf(amountODaysReturner(monthNum.intValue,yearInternal.intValue))}
    var monthNameInternal = remember { mutableStateOf(monthNameReturner(monthNum.intValue)) }
    var visible = remember{ mutableStateOf(false)}

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(15.dp, 15.dp, 15.dp)
        .background(Color.Gray)) {
        Column (
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                //Header; where month year will appear in between month rotation buttons
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
            ) {
                MonthLeftButton(
                    onClick = {
                        if (monthNum.intValue==1){
                            yearInternal.intValue--
                            monthNum.intValue = 12
                        }else monthNum.intValue--
                        todayMonthFDay.value = LocalDate.of(
                            yearInternal.intValue,
                            monthNum.intValue,
                            1
                            ).dayOfWeek.toString()
                        todayMonthFDayNum.value = fDayNumReturner(todayMonthFDay.value)
                        amountOfDays.intValue = amountODaysReturner(monthNum.intValue,yearInternal.intValue)
                        monthNameInternal.value = monthNameReturner(monthNum.intValue)

                    }
                )
                MonthName(
                    modifier = Modifier.weight(1f),
                    month = monthNameInternal.value,
                    year = yearInternal.intValue.toString(),
                    onClick = {visible.value = true}
                    )
                MonthRightButton(
                    onClick = {
                        if (monthNum.intValue==12){
                            yearInternal.intValue++
                            monthNum.intValue = 1
                        }else monthNum.intValue++
                        todayMonthFDay.value = LocalDate.of(
                            yearInternal.intValue,
                            monthNum.intValue,
                            1
                        ).dayOfWeek.toString()
                        todayMonthFDayNum.intValue = fDayNumReturner(todayMonthFDay.value)
                        amountOfDays.intValue = amountODaysReturner(monthNum.intValue,yearInternal.intValue)
                        monthNameInternal.value = monthNameReturner(monthNum.intValue)
                    }
                )
            }
            WeekHeader(width)
            CalendarDayContainer(
                firstDayOfMonth = todayMonthFDayNum.intValue,
                amountOfDays = amountOfDays.intValue,
                colorArray = colorArray,
                width = width,
                eventArray,
                monthNameInternal.value,
                yearInternal.intValue.toString())

        }
    }
    //MothPicker is the AlertDialog that pops up upon launching the activity and is also visible
    //when clicked on the month,year text
    MonthPicker(
        visible = visible.value,
        monthNum = monthNum.intValue,
        year = yearInternal.intValue,
        confirmButtonClicked = {month_, year_ ->
            monthNum.intValue = month_
            yearInternal.intValue = year_
            todayMonthFDay.value = LocalDate.of(
                yearInternal.intValue,
                monthNum.intValue,
                1
            ).dayOfWeek.toString()
            todayMonthFDayNum.intValue = fDayNumReturner(todayMonthFDay.value)
            amountOfDays.intValue = amountODaysReturner(monthNum.intValue,yearInternal.intValue)
            monthNameInternal.value = monthNameReturner(monthNum.intValue)
            visible.value = false
        },
        cancelButtonClicked = {
            visible.value = false
        }
        )
}

@Composable
fun WeekHeader(width: Int) {
    //Header that indicates which date corresponds to which day
    Row(
        Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(10.dp)) {

        val daysArray = arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        for (i in 0 until 7){
            Text(text = daysArray[i],
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.Black,
                modifier = Modifier.width(width.dp)
            )
        }
    }
}

@Composable
fun CalendarDayContainer(
    firstDayOfMonth:Int,
    amountOfDays: Int,
    colorArray: Array<Color>,
    width: Int,
    eventArray: ArrayList<Events>,
    month: String,
    year: String) {

    var internalEventArray = remember { mutableStateOf(ArrayList<Events>()) }
    var day = remember { mutableStateOf(LocalDate.now().dayOfMonth) }
    var visibility = remember { mutableFloatStateOf(0f) }
    val absFirstDay = 1-firstDayOfMonth
    var firstDay = absFirstDay
    val hashDate = dupeDays(eventArray)
    val monthNum = monthNumReturner(month)
    var eventNum = 0

    Column {
        for (i in 1 until 7) {
            var nextLineBool = true
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                while (firstDay <= 7 * i && nextLineBool) {
                    firstDay++
                    nextLineBool = calBreaker(absFirstDay, firstDay, i)
                    if (firstDay in 1..amountOfDays) {
                        eventNum = checkEventAmountPerDay(
                            hashDate,
                            year.toInt(),
                            monthNum,
                            firstDay
                        )
                    }
                    DayBox(
                        dayVal = firstDay,
                        eventNum = eventNum,
                        eventColArr = colorArray,
                        onDayClick = { internalEventArray.value = getDay(LocalDate.of(year.toInt(),monthNum,it))
                            day.value = it
                            if(internalEventArray.value.size!=0){visibility.floatValue = 1f}
                                     },
                        width = width,
                        amountOfDays
                    )

                }
            }
        }
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            for (event in internalEventArray.value){
                EventInfoMenu(
                    event.calendarName,
                    event.calendarDesc1,
                    LocalDate.of(year.toInt(), monthNum,day.value).toString(),
                    visibility.floatValue)
                Log.d("DEBUG", "${event.calendarName}, ${event.calendarDesc1}")
            }

        }
    }
}

@Composable
fun EventInfoMenu(title: String, desc: String, date: String, visibility: Float) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(Color.White)
            .padding(top = 3.dp)
            .alpha(visibility)
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .fillMaxWidth()
        ) {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = desc,
                fontSize = 12.sp,
            )
        }
        Text(
            text = date,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        )

    }

}

@Composable
fun MonthName(
    month: String,
    year: String,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Text(text = "$month, $year",
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = modifier
            .clickable {onClick()}
    )
}
@Composable
fun MonthLeftButton(
    onClick: () -> Unit
) {
    Image(
        painter = painterResource(id = R.drawable.baseline_arrow_left_24),
        contentDescription ="Back Button",
        modifier =
        Modifier
            .size(40.dp)
            .border(2.dp, Color.Black)
            .clickable { onClick() })
}
@Composable
fun MonthRightButton(
    onClick: () -> Unit
) {
    Image(
        painter = painterResource(id =R.drawable.baseline_arrow_right_24),
        contentDescription ="Back Button",
        modifier =
        Modifier
            .size(40.dp)
            .border(2.dp, Color.Black)
            .clickable { onClick() })
}
@Composable
fun DayBox(
    dayVal: Int,
    eventNum: Int,
    eventColArr: Array<Color>,
    onDayClick:(Int)->Unit,
    width: Int,
    amountOfDays: Int,
){
    Column(
        Modifier
            .width(width.dp)
            .height(60.dp)
            .background(Color.White)
            .clickable { onDayClick(dayVal) }) {
        if (dayVal in 1..amountOfDays) {
            Text(
                text = dayVal.toString(),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .offset(0.dp, 5.dp)
                    .fillMaxWidth()
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                verticalArrangement = Arrangement.spacedBy(1.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (eventNum < 6) {
                    for (i in 0 until eventNum) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(3.dp)
                                .background(eventColArr[i]),
                        )

                    }
                } else {
                    for (i in 0 until 4) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(3.dp)
                                .background(eventColArr[i]),
                        )
                    }
                    Image(
                        painter = painterResource(id = R.drawable.baseline_add_24),
                        contentDescription = "More Than 5 events"
                    )
                }
            }
        }
    }
}



@Preview(heightDp = 800)
@Composable
private fun Preview() {
    CalendarParent(
        monthName = "March",
        year = "2024",
        eventArray = eventArrayEvents
    )
}


private fun getDay(day: LocalDate):ArrayList<Events>{
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

private fun calBreaker(absFirstDay: Int, firstDay: Int, i: Int): Boolean {
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

private fun amountODaysReturner(monthNum: Int, year: Int):Int{
    return when(monthNum){
        1,3,5,7,8,10,12 -> 31
        4,6,9,11 -> 30
        2-> if (year%4==0){29}else 28
        else -> {throw IllegalArgumentException("Invalid month number: $monthNum")}
    }
}

private fun fDayNumReturner(dayName:String):Int{
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