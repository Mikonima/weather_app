package com.example.weather_app.fragments

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
//Передача разрешения(Тут одно p, функция проверяет 1 или -1)
//проверка даётся ли вообще разрешение
fun Fragment.isPermissionsGranted(p:String):Boolean{
    return ContextCompat.checkSelfPermission(
        activity as AppCompatActivity,p)==PackageManager.PERMISSION_GRANTED


}