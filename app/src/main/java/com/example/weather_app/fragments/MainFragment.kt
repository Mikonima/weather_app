package com.example.weather_app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.weather_app.adapters.VpAdapte
import com.example.weather_app.databinding.FragmentMainBinding
import com.google.android.material.tabs.TabLayoutMediator

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

//даёт ли пользователь разрешение на исп. местоположения
    private fun permissionListener(){
        pLauncher=registerForActivityResult(
            ActivityResultContracts.RequestPermission()){
            Toast.makeText(activity,"Ощибка $it", Toast.LENGTH_SHORT).show()
            
        }
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