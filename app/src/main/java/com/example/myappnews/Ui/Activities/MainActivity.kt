package com.example.myappnews.Ui.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myappnews.R
import com.example.myappnews.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.reflect.Array.set

class MainActivity : AppCompatActivity() {
    private lateinit var navController:NavController
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var binding:ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        getNowFragment()

    }

    private fun init(){
        navHostFragment=supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController=navHostFragment.navController
        appBarConfiguration= AppBarConfiguration(setOf(R.id.home_Fragment,R.id.search_Fragment,R.id.profile_Fragment))
//        findViewById<BottomNavigationView>(R.id.bottom_app_bar_main).setupWithNavController(navController)
//        binding.viewFlipperBottom.elevation=10.0f
    }
    
    private fun getNowFragment(){
        val navController = findNavController(R.id.nav_host_fragment)
        val destinationChangedListener = NavController.OnDestinationChangedListener { _, destination, _ ->
            when (destination.label) {
                "Home_Fragment"->{
//                    binding.viewFlipperBottom.displayedChild = 0
                }
                "Search_Fragment"->{

                }
                "Profile_Fragment"->{
//                    binding.viewFlipperBottom.displayedChild = 2

                }
                "Article_Fragment"->{
//                    binding.viewFlipperBottom.displayedChild = 1

                }
                else -> "Invalid day"
            }
        }
        navController.addOnDestinationChangedListener(destinationChangedListener)
    }

}