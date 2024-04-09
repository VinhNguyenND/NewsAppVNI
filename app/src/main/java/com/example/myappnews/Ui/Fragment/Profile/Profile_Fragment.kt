package com.example.myappnews.Ui.Fragment.Profile

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.myappnews.Data.Enum.UserEnum
import com.example.myappnews.Data.SharedPreferences.Shared_Preference
import com.example.myappnews.R
import com.example.myappnews.databinding.ProfileScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Profile_Fragment : Fragment() {
    private lateinit var binding: ProfileScreenBinding
    private lateinit var _shared_Preference: Shared_Preference;
    private var auth: FirebaseAuth = Firebase.auth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ProfileScreenBinding.inflate(inflater, container, false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _shared_Preference = Shared_Preference(requireActivity());
        Log.i("hello", UserEnum.User.toString())
        initLayout()
        event();
    }

    override fun onResume() {
        super.onResume()
    }

    private fun navigate(id: Int) {
        Navigation.findNavController(binding.root).navigate(id)
    }

    private fun event() {
        binding.addMin.setOnClickListener {
            when (_shared_Preference.getPermission()) {
                UserEnum.Admin.toString() -> {
                   navigate(R.id.mainAdmFrag)
                }

                UserEnum.User.toString() -> {
                    binding.layoutSpecial.visibility = View.VISIBLE
                }

                UserEnum.Author.toString() -> {
                    navigate(R.id.authorHome)

                }

                UserEnum.Authorized.toString() -> {
                    binding.layoutSpecial.visibility = View.VISIBLE
                }
            }
        }
        binding.DangNhapProfile.setOnClickListener {
            navigate(R.id.loginFragment)
        }
        binding.btnDangXuat.setOnClickListener {
            _shared_Preference.logout()
            auth.signOut()
            binding.layoutSpecial.visibility = View.INVISIBLE
            binding.DangNhapProfile.visibility = View.VISIBLE
        }
    }

    private fun initLayout() {
        if (_shared_Preference.isLogin()) {
            when (_shared_Preference.getPermission()) {
                UserEnum.Admin.toString() -> {
                    binding.layoutSpecial.visibility = View.VISIBLE
                }

                UserEnum.User.toString() -> {
                    binding.layoutSpecial.visibility = View.VISIBLE
                }

                UserEnum.Author.toString() -> {
                    binding.layoutSpecial.visibility = View.VISIBLE
                }

                UserEnum.Authorized.toString() -> {
                    binding.layoutSpecial.visibility = View.VISIBLE
                }
            }
        } else {
            binding.DangNhapProfile.visibility = View.VISIBLE
        }
    }


    fun showToast(context: Context, message: String) {
        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = layoutInflater.inflate(R.layout.toast_is_succes, null)

        val textViewMessage = layout.findViewById<TextView>(R.id.textViewMessage)
        textViewMessage.text = message

        val toast = Toast(context)
        toast.setGravity(
            Gravity.CENTER_VERTICAL or Gravity.BOTTOM,
            0,
            100
        )  // Thiết lập vị trí hiển thị của Toast
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.show()
    }
}