package com.example.myappnews.Ui.Fragment.LoginSignUp

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.example.myappnews.Data.Firebase.ViewModel.ArticleViewModel.ArViewModel
import com.example.myappnews.Data.Local.Article.ArticlelocalViewModel
import com.example.myappnews.R
import com.example.myappnews.databinding.LoginScreenBinding

class LoginFragment : Fragment() {

    private lateinit var binding: LoginScreenBinding

    private val articleViewModel = ArViewModel.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LoginScreenBinding.inflate(inflater, container, false);
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        event()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun event() {
        binding.toCreateAccount.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.signFragment)
        }
        binding.btnDangNhap.setOnClickListener {
            binding.progressCircularLogin.visibility = View.VISIBLE
            articleViewModel.SignIn(
                requireActivity(),
                binding.txtEmailLogin.text.toString(),
                binding.txtPassWordLogin.text.toString()
            ).observe(viewLifecycleOwner, Observer {
                    binding.progressCircularLogin.visibility = View.INVISIBLE
                    if (it == 1) {
                        Navigation.findNavController(binding.root).popBackStack();
                        showToast(requireContext(), "Đăng Nhập thành công")
                    } else if (it == 0) {
                        showToast(requireContext(), "Đăng Nhập Thất Bại")
                    } else {
                        showToast(requireContext(), "Lỗi Đăng Nhập")
                    }
                })
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