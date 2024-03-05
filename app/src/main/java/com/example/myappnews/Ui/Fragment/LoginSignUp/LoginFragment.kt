package com.example.myappnews.Ui.Fragment.LoginSignUp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myappnews.databinding.LoginScreenBinding

class LoginFragment : Fragment() {

    private lateinit var binding:LoginScreenBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=LoginScreenBinding.inflate(inflater,container,false);
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}