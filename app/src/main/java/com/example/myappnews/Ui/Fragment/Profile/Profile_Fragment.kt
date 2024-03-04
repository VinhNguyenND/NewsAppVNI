package com.example.myappnews.Ui.Fragment.Profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myappnews.databinding.ProfileScreenBinding

class Profile_Fragment:Fragment() {
    private lateinit var binding:ProfileScreenBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=ProfileScreenBinding.inflate(inflater,container,false);
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}