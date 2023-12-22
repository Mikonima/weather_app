package com.example.weather_app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.weather_app.databinding.FragmentMainBinding

//главный фрагмент
class MainFragment : Fragment() {
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