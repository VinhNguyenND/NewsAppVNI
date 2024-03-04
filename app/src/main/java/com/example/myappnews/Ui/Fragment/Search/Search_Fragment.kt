package com.example.myappnews.Ui.Fragment.Search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myappnews.databinding.SearchScreenBinding

class Search_Fragment:Fragment() {
    private lateinit var binding:SearchScreenBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=SearchScreenBinding.inflate(inflater,container,false);
        return binding.root
    }
}