package com.example.weather_app

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel:ViewModel() {
    val liveDataCurrent=MutableLiveData<String>()
    val liveDataList=MutableLiveData<List<String>>()
}