package com.example.composeweb
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.composeweb.data.WeatherInfo
import com.example.composeweb.logic.WeatherViewModel
import androidx.compose.foundation.lazy.items
class MainActivity : ComponentActivity() {
    private lateinit var weatherViewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var city="北京"
            val weatherInfo= remember {
                mutableStateOf<WeatherInfo?>(null)
            }
            weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
            weatherViewModel.getWeatherData()
            weatherViewModel.weatherLiveData.observe(this, Observer { weather ->
               weatherInfo.value= giveValue(weather)
                Log.d("MainActivityLogout","main ${weatherInfo.value?.result?.realtime?.info}")

            })
            MainUI(weatherInfo.value,city)
            Log.d("MainActivityLogout","mainUI ${weatherInfo.value?.result?.realtime?.info}")
            }
    }
}

fun giveValue(weatherInfo: WeatherInfo? ):WeatherInfo? {

    return weatherInfo
}

@Composable
fun MainUI(weatherInfo: WeatherInfo? ,city:String){

    Column() {
        CityCard(city)
        Spacer(modifier = Modifier.height(16.dp))
        WeatherCard(weatherInfo)
        Spacer(modifier = Modifier.height(16.dp))
        weatherInfo?.result?.future?.let { futureList ->
            getFutureCard(futureList)
        }//进行非空判断
        }
    }
@Composable
fun getFutureCard(fu:List<WeatherInfo.Future1>){
    LazyColumn{
        items(fu){
            fu->
            FutureCard(fu)
        }
    }
}
@Composable
fun CityCard(city:String ) {
    val date = LocalDate.now().toString()
    Row() {
        Surface(shape = MaterialTheme.shapes.medium, elevation = 1.dp,
            modifier = Modifier
                .size(360.dp, 120.dp)
                .padding(10.dp)
        ) {
            Row() {
                Image(
                    painter = painterResource(id = R.drawable.city),
                    contentDescription = "city",
                    modifier = Modifier
                        .size(80.dp)
                )
                Column() {
                    Text(
                        text = city,
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .padding(bottom = 2.dp)
                            .padding(start = 40.dp),
                        style = MaterialTheme.typography.body2,
                        color = Color.Black,
                        fontSize = 20.sp
                    )
                    Text(
                        text = date,
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .padding(start = 40.dp),
                        style = MaterialTheme.typography.body2,
                        color = Color.Black,
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}
@Composable
fun WeatherCard(weatherInfo: WeatherInfo? )
{
//温度，湿度，info，风（方向，大小），aqi
    Surface(shape = MaterialTheme.shapes.medium, elevation = 1.dp,
        modifier = Modifier
            .size(360.dp, 200.dp)
            .padding(10.dp)
    ) {
        Row() {
            Surface(shape = MaterialTheme.shapes.medium, elevation = 1.dp,
                modifier = Modifier
                    .size(160.dp, 160.dp)
                    .padding(10.dp)) {
                Column() {
                    Surface(shape = MaterialTheme.shapes.medium,
                        modifier = Modifier
                            .size(160.dp, 110.dp)
                            ) {
                    Image(//根据天气替换图片
                        painter = painterResource(id =
                        when (weatherInfo?.result?.realtime?.info){
                            "晴"->R.drawable.w00
                            "多云"->R.drawable.w01
                            "阵雨","小到中雨","中到大雨"->R.drawable.w032122
                            "雷阵雨"->R.drawable.w04
                            "雷阵雨伴有冰雹","雨夹雪","冻雨"->R.drawable.w050619
                            "小雨"->R.drawable.w07
                            "中雨"->R.drawable.w08
                            "大雨","暴雨","大暴雨","特大暴雨","大到暴雨","暴雨到大暴雨","大暴雨到特大暴雨"->R.drawable.w09101112232425
                            "阵雪","大雪","暴","小到中雪","中到大雪","大到暴雪"->R.drawable.w131617262728
                            "小雪","中雪"->R.drawable.w1415
                            "雾","霾"->R.drawable.w1853
                            "沙尘暴","浮尘","扬沙","强沙尘暴"->R.drawable.w20293031
                            else->R.drawable.wind
                        }
                        ),
                        contentDescription = "weather",
                        modifier = Modifier)
                    }

                    Surface(shape = MaterialTheme.shapes.medium,
                        modifier = Modifier
                            .size(160.dp, 40.dp)
                    ) {
                        Text(
                            text = weatherInfo?.result?.realtime?.info ?: "null",
                            modifier = Modifier
                                .padding(top = 2.dp)
                                .padding(start = 2.dp),
                            style = MaterialTheme.typography.body2,
                            color = Color.Black,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

            }
            Column() {
                Surface(shape = MaterialTheme.shapes.medium, elevation = 1.dp,
                    modifier = Modifier
                        .size(160.dp, 40.dp)
                        .padding(start = 10.dp)
                        .padding(bottom = 4.dp)
                        .padding(top = 10.dp)) {
                    Row() {
                        Image(//温度
                            painter = painterResource(id = R.drawable.temp),
                            contentDescription = "温度",
                            modifier = Modifier
                                .size(40.dp)
                        )
                        Text(
                            text = "温度: ${weatherInfo?.result?.realtime?.temperature}",
                            modifier = Modifier
                                .padding(top = 2.dp)
                                .padding(start = 2.dp),
                            style = MaterialTheme.typography.body2,
                            color = Color.Black,
                            fontSize = 20.sp ,


                        )
                    }
                }
                Surface(shape = MaterialTheme.shapes.medium, elevation = 1.dp,
                    modifier = Modifier
                        .size(160.dp, 40.dp)
                        .padding(start = 10.dp)
                        .padding(bottom = 4.dp)
                        .padding(top = 10.dp)) {
                    Row() {
                        Image(//湿度
                            painter = painterResource(id = R.drawable.shidu),
                            contentDescription = "湿度",
                            modifier = Modifier
                                .size(40.dp)
                        )
                        Text(
                            text = "湿度: ${weatherInfo?.result?.realtime?.humidity}",
                            modifier = Modifier
                                .padding(top = 2.dp)
                                .padding(start = 2.dp),
                            style = MaterialTheme.typography.body2,
                            color = Color.Black,
                            fontSize = 20.sp

                        )
                    }
                }
                Surface(shape = MaterialTheme.shapes.medium, elevation = 1.dp,
                    modifier = Modifier
                        .size(160.dp, 40.dp)
                        .padding(start = 10.dp)
                        .padding(bottom = 4.dp)
                        .padding(top = 10.dp)) {
                    Row() {
                        Image(//风向
                            painter = painterResource(id = R.drawable.wind),
                            contentDescription = "wind",
                            modifier = Modifier
                                .size(40.dp)
                        )
                        Text(
                            text = "风向: ${weatherInfo?.result?.realtime?.direct}",
                            modifier = Modifier
                                .padding(top = 2.dp)
                                .padding(start = 2.dp),
                            style = MaterialTheme.typography.body2,
                            color = Color.Black,
                            fontSize = 20.sp

                        )
                    }
                }
                Surface(shape = MaterialTheme.shapes.medium, elevation = 1.dp,
                    modifier = Modifier
                        .size(160.dp, 40.dp)
                        .padding(start = 10.dp)
                        .padding(bottom = 4.dp)
                        .padding(top = 10.dp)) {
                    Row() {
                        Image(//aqi
                            painter = painterResource(id = R.drawable.aqi),
                            contentDescription = "aqi",
                            modifier = Modifier
                                .size(40.dp)
                        )
                        Text(
                            text = "空气质量: ${weatherInfo?.result?.realtime?.aqi}",
                            modifier = Modifier
                                .padding(top = 2.dp)
                                .padding(start = 2.dp),
                            style = MaterialTheme.typography.body2,
                            color = Color.Black,
                            fontSize = 20.sp

                        )
                    }
                }
            }
        }
    }
    }


@Composable
fun FutureCard(fu:WeatherInfo.Future1 ) {
    Surface(
        shape = MaterialTheme.shapes.medium, elevation = 1.dp,
        modifier = Modifier
            .size(360.dp, 160.dp)
            .padding(10.dp)
    ) {
        Row() {
                Column() {
                    Surface(shape = MaterialTheme.shapes.medium, elevation = 1.dp,
                        modifier = Modifier
                            .size(200.dp, 40.dp)
                            .padding(start = 10.dp)
                            .padding(bottom = 4.dp)
                            .padding(top = 10.dp)) {
                        Row() {
                            Image(//温度
                                painter = painterResource(id = R.drawable.temp),
                                contentDescription = "temperature",
                                modifier = Modifier
                                    .size(40.dp)
                            )
                            Text(
                                text = "温度: ${fu.temperature}",
                                modifier = Modifier
                                    .padding(top = 2.dp)
                                    .padding(start = 2.dp),
                                style = MaterialTheme.typography.body2,
                                color = Color.Black,
                                fontSize = 20.sp

                            )
                        }
                    }
                    Surface(shape = MaterialTheme.shapes.medium, elevation = 1.dp,
                        modifier = Modifier
                            .size(200.dp, 40.dp)
                            .padding(start = 10.dp)
                            .padding(bottom = 4.dp)
                            .padding(top = 10.dp)) {
                        Row() {
                            Image(//weather
                                painter = painterResource(id =
                                when (fu.weather){
                                    "晴"->R.drawable.w00
                                    "多云"->R.drawable.w01
                                    "阵雨","小到中雨","中到大雨"->R.drawable.w032122
                                    "雷阵雨"->R.drawable.w04
                                    "雷阵雨伴有冰雹","雨夹雪","冻雨"->R.drawable.w050619
                                    "小雨"->R.drawable.w07
                                    "中雨"->R.drawable.w08
                                    "大雨","暴雨","大暴雨","特大暴雨","大到暴雨","暴雨到大暴雨","大暴雨到特大暴雨"->R.drawable.w09101112232425
                                    "阵雪","大雪","暴","小到中雪","中到大雪","大到暴雪"->R.drawable.w131617262728
                                    "小雪","中雪"->R.drawable.w1415
                                    "雾","霾"->R.drawable.w1853
                                    "沙尘暴","浮尘","扬沙","强沙尘暴"->R.drawable.w20293031
                                    else->R.drawable.w01
                                }),
                                contentDescription = "aqi",
                                modifier = Modifier
                                    .size(40.dp)
                            )
                            Text(
                                text = "天气: ${fu.weather}",
                                modifier = Modifier
                                    .padding(top = 2.dp)
                                    .padding(start = 2.dp),
                                style = MaterialTheme.typography.body2,
                                color = Color.Black,
                                fontSize = 20.sp

                            )
                        }
                    }
                    Surface(shape = MaterialTheme.shapes.medium, elevation = 1.dp,
                        modifier = Modifier
                            .size(200.dp, 40.dp)
                            .padding(start = 10.dp)
                            .padding(bottom = 4.dp)
                            .padding(top = 10.dp)) {
                        Row() {
                            Image(//风向
                                painter = painterResource(id = R.drawable.wind),
                                contentDescription = "wind",
                                modifier = Modifier
                                    .size(40.dp)
                            )
                            Text(
                                text = "风向: ${fu.direct}",
                                modifier = Modifier
                                    .padding(top = 2.dp)
                                    .padding(start = 2.dp),
                                style = MaterialTheme.typography.body2,
                                color = Color.Black,
                                fontSize = 20.sp

                            )
                        }
                    }
                }
            Spacer(modifier = Modifier.width(16.dp))
            Column() {
                Surface(
                    shape = MaterialTheme.shapes.medium, elevation = 1.dp,
                    modifier = Modifier
                        .size(130.dp, 100.dp)
                        .padding(10.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.date),
                        contentDescription = "date",
                        modifier = Modifier
                            .size(80.dp)
                    )
                }
                Surface(
                    shape = MaterialTheme.shapes.medium, elevation = 1.dp,
                    modifier = Modifier
                        .size(120.dp, 20.dp)
                ) {
                    Text(
                        text = fu.date,
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .padding(start = 2.dp),
                        style = MaterialTheme.typography.body2,
                        color = Color.Black,
                        fontSize = 20.sp
                    )

                }
            }

        }
    }
}
@Composable
fun EditText( ){
    val textValue = remember { mutableStateOf("") }
    val primaryColor = colorResource(id = R.color.white)
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ){
        OutlinedTextField(

            label = {
                Text(
                    text = "请输入想要查询的城市",
                    color = MaterialTheme.colors.secondaryVariant
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = primaryColor,
                focusedLabelColor = Color.Gray,
                cursorColor = primaryColor
            ),
            modifier = Modifier
                .size(290.dp,60.dp),
            maxLines = 2,
            value = textValue.value,
            onValueChange = {
                textValue.value = it
            }
        )
        val city=textValue.value

        Button(
            onClick = {
               //getJson(city)
                textValue.value=""
                Log.d("MainActivitySHowwww","click")
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.white)),
            border = BorderStroke(
                1.dp,
                color = colorResource(id = R.color.black)
            )
        ) {
            Text(
                text = "搜索",
                color= MaterialTheme.colors.secondaryVariant,
            )
        }
    }
}

@Composable
fun showDate(){
    val time=LocalDate.now().toString()
    Column(){
        Row(){
            Image(
                painter = painterResource(id = R.drawable.fck),
                contentDescription = "fck picture",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, MaterialTheme.colors.secondary, CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))
           Column() {
               Text(
                   text = time,

                   color = MaterialTheme.colors.secondaryVariant,
                   fontSize = 15.sp,
                   style = MaterialTheme.typography.subtitle2

               )
               Spacer(modifier = Modifier.height(1.dp))
               Text(
                   text = "weather forecast",
                   color = MaterialTheme.colors.secondaryVariant,
                   fontSize = 20.sp,
                   style = MaterialTheme.typography.subtitle2

               )
           }
        }
    }
}
fun notNull(message:String?):String{ return message?:"Null" }
