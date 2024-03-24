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
                        todayMonthFDayNum.intValue = fDayNumReturner(todayMonthFDay.value)
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
            .padding(vertical = 10.dp)) {

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
        // 6 rows of weeks(7 day boxes) are created.
        for (i in 1 until 7) {
            var nextLineBool = true
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                //first day value indicates what value the monday of the first month should have,
                //for instance if a months first day is wednesday firstDay value shall be -1
                //so when the days are set monday will be -1 tuesday will be 0 and then wednesday
                //will appear
                while (firstDay <= 7 * i && nextLineBool) {
                    //this loop checks if the day value is within the week it should be
                    //and it breaks with the help of nextLineBool.
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
                        onDayClick = {
                            //under this onClick method the internalEventArray is being filled with
                            //the selected day's events and if the internalEventArray is empty sets
                            internalEventArray.value = getDay(
                                LocalDate.of(
                                    year.toInt(),
                                    monthNum,
                                    it))
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
                //for every event in the clicked day an event infomenu will be called in this
                //scrollable column
                EventInfoMenu(
                    event.calendarName,
                    event.calendarDesc1,
                    LocalDate.of(year.toInt(), monthNum,day.value).toString(),
                    visibility.floatValue)
            }

        }
    }
}

@Composable
fun EventInfoMenu(title: String, desc: String, date: String, visibility: Float) {

    //An information menu utilized to portray event information depending on the day clicked
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
    //Text composable to show the month and year value in a Month, Year format
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
    //Button used for decreasing the month value
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
    //Button used for increasing the month value
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
    //The day box that contains what day of the month it is and does contain stripes to declare
    //how many events in a day there are
    Column(
        Modifier
            .width(width.dp)
            .height(60.dp)
            .background(Color.White)
            .clickable(enabled = dayVal in 1..amountOfDays) {onDayClick(dayVal)}) {
        if (dayVal in 1..amountOfDays) {
            //since the dayVal can be a negative number or more than 31 we limit it so that
            //negative numbers or day numbers more than the amount of days in a month doesn't appear
            Text(
                text = dayVal.toString(),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .offset(0.dp, 5.dp)
                    .fillMaxWidth()
            )
            //below the text a column is created to contain the event stripes
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                verticalArrangement = Arrangement.spacedBy(1.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                //under these conditions number of events is checked and if it's less than 6
                //up to 5 stripes will appear, but if it's more than 5, 4 stripes and a plus icon
                //will appear meaning we have more than 5 events.
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


