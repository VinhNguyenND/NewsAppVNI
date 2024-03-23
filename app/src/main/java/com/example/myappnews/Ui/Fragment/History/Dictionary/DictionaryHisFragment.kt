package com.example.myappnews.Ui.Fragment.History.Dictionary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myappnews.databinding.DictionaryHistoryBinding

class DictionaryHisFragment:Fragment() {
    private lateinit var binding: DictionaryHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=DictionaryHistoryBinding.inflate(layoutInflater,container,false);
        return binding.root;
    }
}