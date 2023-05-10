package com.example.composeweb.data

import kotlinx.serialization.Serializable

@Serializable
data class WeatherInfo (//根据api文档写的数据类
    val reason: String,
    val result: Result,
    val error_code: Int
) {
    @Serializable
    data class Result(
        //val city:String(在官方的api文档里没有city这一行，但是在返回结果中是有的，先注释掉）
        val realtime: Realtime,
        val future: List<Future1>
    )
    @Serializable
    data class Future1(//太逆天了，future就报错半天，改成1就行
        val date: String,
        val temperature: String,
        val weather: String,
        val direct: String
    )

    @Serializable
    data class Realtime(
        val temperature: String,
        val humidity: String,
        val info: String,
        val direct: String,
        val aqi: String
    )
}
