package com.example.composeweb.logic

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.composeweb.data.WeatherInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
//总体上使用了MVVM框架，这里是ViewModel
interface WeatherService{
    @GET("simpleWeather/query")
    fun getWeatherData(@Query("city") city:String, @Query("key") key:String ): Call<WeatherInfo>
}

class WeatherViewModel: ViewModel() {
    private val _weatherLiveData = MutableLiveData<WeatherInfo>()
    val weatherLiveData: LiveData<WeatherInfo>
        get() = _weatherLiveData

    fun getWeatherData() {
        val url = "http://apis.juhe.cn/"
        val key = "4621d831a922538574c424704f83af06"
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        Log.d("MainActivitySHowwww", "getJson")
        val weatherService = retrofit.create(WeatherService::class.java)

        weatherService.getWeatherData("北京", key).enqueue(object : Callback<WeatherInfo> {
            override fun onResponse(
                call: Call<WeatherInfo>,
                response: Response<WeatherInfo>
            ) {
                if (response.isSuccessful) {
                    val weather = response.body()
                    _weatherLiveData.postValue(weather)
                    Log.d("MainActivitySHowwww", "success")

                    if (weather != null)
                    {Log.d("MainActivitySHowwww", "reason is ${weather.reason}")
                    Log.d("MainActivitySHowwww", "reason is ${weather?.result?.realtime?.info}")}
                    else Log.d("MainActivitySHowwww", "reason is null")
                } else {
                    Log.e("MainActivitySHowwww", response.code().toString())
                }
            }

            override fun onFailure(call: Call<WeatherInfo>, t: Throwable) {
                Log.d("MainActivitySHowwww", "Failure")
                Log.e("MainActivitySHowwww", "Other Exception: ${t.message}")
                t.printStackTrace()
            }
        })
    }
}
fun getWeather(weather: WeatherInfo?): WeatherInfo?
{
    return weather
}