package com.example.educarecalendar

import android.content.Context
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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


@Composable
fun CalendarParent(
    monthName: String,
    year: String,
    eventArray: ArrayList<Events>,
    context: Context,
    modifier: Modifier
) {
    val colorArray: Array<Color> = arrayOf(Col1,Col2,Col3,Col4,Col5)
    val width = (LocalConfiguration.current.screenWidthDp - 25)/7
    var firstDayOfMonth = 1
    var amountOfDays = 31
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
                MonthLeftButton()
                MonthName(
                    modifier = Modifier.weight(1f),
                    month = monthName,
                    year = year
                    )
                MonthRightButton()
            }
            WeekHeader(width)
            val handler = Handler()
            CalendarDayContainer(
                firstDayOfMonth = 7,
                amountOfDays = 31,
                colorArray = colorArray,
                width = width,
                eventArray,
                monthName,
                year)

        }
    }
}

@Composable
fun WeekHeader(width: Int) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(Color.White)) {

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

    val absFirstDay = 1-firstDayOfMonth
    var firstDay = absFirstDay

    Log.d("eventArraySizeInCalendar.kt","${eventArray.size}")

    val hashDate = dupeDays(eventArray)

    val monthNum = monthNumReturner(month)
    var eventNum = 0


    for (i in 1 until 7) {
        var nextLineBool = true
        Row(
            Modifier
                .fillMaxWidth()
                .background(Color.White)) {

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
                        onDayClick = { printDay(it) },
                        width = width,
                        amountOfDays
                    )

                }

        }
    }
}
@Composable
fun MonthName(
    month: String,
    year: String,
    modifier: Modifier
) {
    Text(text = "$month, $year",
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}
@Composable
fun MonthLeftButton() {
    Image(
        painter = painterResource(id = R.drawable.baseline_arrow_left_24),
        contentDescription ="Back Button",
        modifier =
        Modifier
            .size(40.dp)
            .border(2.dp, Color.Black))
}
@Composable
fun MonthRightButton() {
    Image(
        painter = painterResource(id =R.drawable.baseline_arrow_right_24),
        contentDescription ="Back Button",
        modifier =
        Modifier
            .size(40.dp)
            .border(2.dp, Color.Black))
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
    //CalendarParent(modifier = Modifier.fillMaxSize())
}


private fun printDay(day:Int){Log.d("printDay","$day")}

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

private fun monthNumReturner(monthName: String): Int{
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