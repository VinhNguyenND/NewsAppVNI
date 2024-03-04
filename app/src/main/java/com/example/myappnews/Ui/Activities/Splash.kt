package com.example.myappnews.Ui.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.example.myappnews.R
import com.example.myappnews.databinding.ActivityMainBinding
import com.example.myappnews.databinding.ActivitySplashBinding

class Splash : AppCompatActivity() {
    private lateinit var binding:ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        hideStatusBar(this.window);
    }
}
private fun hideStatusBar(window:Window){
    window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
}