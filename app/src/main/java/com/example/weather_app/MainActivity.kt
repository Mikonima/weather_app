package com.example.weather_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.voice.VoiceInteractionSession.Request
import android.util.Log
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.weather_app.databinding.ActivityMainBinding
import kotlin.math.log

const val API_KEY="0b6068d8628b40c6b7a130405232212"//Константа ключ AP WeatherAPI.com
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding //buildFeatures viewBinding = true
    override fun onCreate(savedInstanceState: Bundle?) {
//тест
        super.onCreate(savedInstanceState)

        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bGet.setOnClickListener{
            getResult("Krasnoyarsk")

        }//Слушатель на экранную кнопку
    }
    private fun getResult(name:String){
        val url ="https://api.weatherapi.com/v1/current.json" +
                "?key=$API_KEY&q=$name&aqi=no"
        val queue =Volley.newRequestQueue(this  )//очередь на запуск ссылки
        val stringRequest = StringRequest(com.android.volley.Request.Method.GET,
            url,
            {
             response->
                Log.d("MyLog","Успех:$response")//результат успеха

            },
            {
                Log.d("MyLog","Ошибка:$it")//результат ошибки
            }
        )
        queue.add(stringRequest)

    }//функция запроса и возврата
}