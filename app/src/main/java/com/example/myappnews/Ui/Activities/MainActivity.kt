package com.example.myappnews.Ui.Activities

import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myappnews.Data.Api.Internet.InternetViewModel
import com.example.myappnews.Data.BroadCast.NetWorkChange.NetworkChangeListener
import com.example.myappnews.Data.BroadCast.NetWorkChange.NetworkChangeReceiver
import com.example.myappnews.R
import com.example.myappnews.Ui.Fragment.management.Author.Home.showToast
import com.example.myappnews.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.reflect.Array.set

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var binding: ActivityMainBinding
    private lateinit var internetViewModel :InternetViewModel
    private var networkChangeReceiver: NetworkChangeReceiver? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        observerInternet()
        getNowFragment()
    }

    private fun init() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.home_Fragment,
                R.id.downLoadedFragment,
                R.id.search_Fragment,
                R.id.profile_Fragment
            )
        )
        findViewById<BottomNavigationView>(R.id.bottom_app_bar_main).setupWithNavController(
            navController
        )

    }

    private fun observerInternet() {
        internetViewModel = ViewModelProvider(this)[InternetViewModel::class.java]
        networkChangeReceiver = NetworkChangeReceiver(object : NetworkChangeListener {
            override fun onNetworkChanged(isConnected: Boolean) {
                internetViewModel.setChangeInternet(isConnected)
            }
        })

        registerReceiver(
            networkChangeReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    private fun getNowFragment() {
        val navController = findNavController(R.id.nav_host_fragment)
        val destinationChangedListener =
            NavController.OnDestinationChangedListener { _, destination, _ ->
                when (destination.label) {
                    "Home_Fragment" -> {
                        binding.bottomMain.visibility = View.VISIBLE
                    }

                    "Search_Fragment" -> {
                        binding.bottomMain.visibility = View.VISIBLE
                    }

                    "Profile_Fragment" -> {
                        binding.bottomMain.visibility = View.VISIBLE
                    }

                    "DownLoadedFragment" -> {
                        binding.bottomMain.visibility = View.VISIBLE
                    }

                    "Article_Fragment" -> {
                        binding.bottomMain.visibility = View.INVISIBLE
                    }

                    "HomeAdmFrag" -> {
                        binding.bottomMain.visibility = View.INVISIBLE
                    }

                    "MainAdmFrag" -> {
                        binding.bottomMain.visibility = View.INVISIBLE
                    }

                    "LoginFragment" -> {
                        binding.bottomMain.visibility = View.INVISIBLE
                    }

                    "SignFragment" -> {
                        binding.bottomMain.visibility = View.INVISIBLE
                    }

                    "AuthorHome" -> {
                        binding.bottomMain.visibility = View.INVISIBLE
                    }

                    "AddNote" -> {
                        binding.bottomMain.visibility = View.INVISIBLE
                    }

                    "NotesFolder" -> {
                        binding.bottomMain.visibility = View.INVISIBLE
                    }

                    "ArticleDown" -> {
                        binding.bottomMain.visibility = View.INVISIBLE
                    }

                    "SearchFragment" -> {
                        binding.bottomMain.visibility = View.INVISIBLE
                    }

                    else -> "Invalid day"
                }
            }
        navController.addOnDestinationChangedListener(destinationChangedListener)
    }

}