package com.example.weather_app.fragments

import android.os.Bundle
import android.service.voice.VoiceInteractionSession.Request
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.weather_app.MainViewModel
import com.example.weather_app.adapters.VpAdapte
import com.example.weather_app.adapters.WeatherModel
import com.example.weather_app.databinding.FragmentMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import org.json.JSONObject
import kotlin.math.log

const val API_KEY="0b6068d8628b40c6b7a130405232212"
//главный фрагмент
class MainFragment : Fragment() {
    private val flist= listOf(
        HoursFragment.newInstance(),
        DaysFragment.newInstance()
    )
    private val tList= listOf(
        "HOURS",
        "DAYS"
    )
    private lateinit var pLauncher:ActivityResultLauncher<String>
    private lateinit var binding: FragmentMainBinding
    private val model: MainViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentMainBinding.inflate(inflater, container, false)

        return binding.root
    }

// проверка(запускается) разрешение на местоположение
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        permissionListener()
        checkPermission()
        init()
        updateCurrentCard()
        requestWeatherData("Krasnoyarsk")




    }
    //переключение табло
    private fun init()=with(binding){
        val adapter=VpAdapte(activity as FragmentActivity,flist)
        vp.adapter=adapter
        TabLayoutMediator(tabLayout,vp)
        {
            tab,pos->tab.text=tList[pos]
        }.attach()
    }
    //заполнение карточки
    private fun updateCurrentCard()= with(binding){
        model.liveDataCurrent.observe(viewLifecycleOwner){
            val maxMinTemp="${it.maxTemp} C /${it.minTemp} C"
            tvData.text=it.time
            tvCity.text=it.city
            tvcurrentTemp.text=it.currentTemp
            tvCondition.text=it.condition
            tvMaxMin.text=maxMinTemp
            Picasso.get().load("https:"+it.imageUrl).into(imWeather)

        }
    }
//даёт ли пользователь разрешение на исп. местоположения
    private fun permissionListener(){
        pLauncher=registerForActivityResult(
            ActivityResultContracts.RequestPermission()){
            Toast.makeText(activity,"Ощибка $it", Toast.LENGTH_SHORT).show()
            
        }
    }
    private fun requestWeatherData(city: String){
        val url="https://api.weatherapi.com/v1/forecast.json?key=" +
                API_KEY +
                "&q=" +
                city +
                "&days=" +
                "3" +
                "&aqi=no&alerts=no"
    val queue=Volley.newRequestQueue(context)
    val request=StringRequest(
        com.android.volley.Request.Method.GET,url,
        {
            result->
            parseWeatherData(result)
        },
        {
            error->

        }
    )
    queue.add(request)
    }
    //Вытаскивание информации из JSON ФАЙЛА
    private fun parseWeatherData(result:String){
        val mainObject=JSONObject(result)
        val list=parseDays(mainObject)
        parseCurrentData(mainObject,list[0])

    }

    private fun parseDays(mainObject: JSONObject):List<WeatherModel>{
        val list=ArrayList<WeatherModel>()
        val daysArray=mainObject.getJSONObject("forecast")
            .getJSONArray("forecastday")
        val name=mainObject.getJSONObject("location").getString("name")
        for (i in 0 until daysArray.length()) {
            val day=daysArray[i] as JSONObject
            val item=WeatherModel(
                name,
                day.getString("date"),
                day.getJSONObject("day").getJSONObject("condition")
                    .getString("text"), currentTemp = "",
                day.getJSONObject("day").getString("maxtemp_c"),
                day.getJSONObject("day").getString("mintemp_c"),
                day.getJSONObject("day").getJSONObject("condition").getString("icon"),
                day.getJSONArray("hour").toString()


            )
            list.add(item)


        }
        return list
    }
    private fun parseCurrentData(mainObject:JSONObject,weatherItem:WeatherModel){

        val item=WeatherModel(
            mainObject.getJSONObject("location").getString("name"),
            mainObject.getJSONObject("current").getString("last_updated"),
            mainObject.getJSONObject("current").getJSONObject("condition").getString("text"),
            mainObject.getJSONObject("current").getString("temp_c"),
            weatherItem.maxTemp,
            weatherItem.minTemp,
            mainObject.getJSONObject("current").getJSONObject("condition").getString("icon"),
            weatherItem.hours
        )
        model.liveDataCurrent.value=item
        Log.d("MyLog","city: ${item.maxTemp}")
        Log.d("MyLog","city: ${item.minTemp}")
        Log.d("MyLog","city: ${item.hours}")
    }

    //если нет разрешения то с помощью лаунчера спросить ответ
    private fun checkPermission(){
        if(!isPermissionsGranted(android.Manifest.permission.ACCESS_FINE_LOCATION)){
            permissionListener()
            pLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)

        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            MainFragment()
    }
}