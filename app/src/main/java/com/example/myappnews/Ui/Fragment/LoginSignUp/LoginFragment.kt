package com.example.myappnews.Ui.Fragment.LoginSignUp

import android.content.Context
import android.os.Bundle
import android.util.Patterns
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
import com.example.myappnews.Data.constant.dismissKeyboard
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
        observer()
        event(view)
    }

    override fun onResume() {
        super.onResume()
    }

    private fun observer() {
        articleViewModel.OberverSignIn().observe(viewLifecycleOwner, Observer {
            if (it == 1) {
                Navigation.findNavController(binding.root).popBackStack();
                binding.progressCircularLogin.visibility = View.GONE
                showToast(requireContext(), "Đăng Nhập thành công")
                articleViewModel.resetLogin(2)
            } else if (it == -1) {
                binding.progressCircularLogin.visibility = View.GONE
                showToast(requireContext(), "Nhập sai mật khẩu hoặc email")
                articleViewModel.resetLogin(2)
            } else if (it == 0) {
                binding.progressCircularLogin.visibility = View.GONE
                showToast(requireContext(), "Tài khoản không tồn tại");
                articleViewModel.resetLogin(2)
            }
        })

        articleViewModel.observerForGot().observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.progressCircularLogin.visibility = View.GONE
                showToast(requireContext(), "Đã gửi, vui lòng kiểm tra email")
            } else {
                binding.progressCircularLogin.visibility = View.GONE
                showToast(requireContext(), "Gửi yêu cầu thất bại")
            }
        })
    }

    private fun event(view: View) {
        binding.toCreateAccount.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.signFragment)
        }
        binding.btnBack.setOnClickListener {
            Navigation.findNavController(binding.root).popBackStack()
        }

        binding.btnDangNhap.setOnClickListener {
            dismissKeyboard(requireContext(), view)
            var isValid = true
            if (binding.txtEmailLogin.text.toString().isNullOrEmpty()) {
                binding.emailInputLayout.error = "Email cannot be empty"
                isValid = false
            } else if (!isValidEmail(binding.txtEmailLogin.text.toString())) {
                binding.emailInputLayout.error = "Please enter a valid email address"
                isValid = false
            } else {
                binding.emailInputLayout.error = null
            }
            if (binding.txtPassWordLogin.text.toString().isNullOrEmpty()) {
                binding.passwordInputLayout.error = "Password cannot be empty"
                isValid = false
            } else if (!isValidPassword(binding.txtPassWordLogin.text.toString())) {
                binding.passwordInputLayout.error =
                    "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one number, and one special character"
                isValid = false
            } else {
                binding.passwordInputLayout.error = null
            }
            if (isValid) {
                binding.progressCircularLogin.visibility = View.VISIBLE
                articleViewModel.SignIn(
                    requireActivity(),
                    binding.txtEmailLogin.text.toString(),
                    binding.txtPassWordLogin.text.toString()
                )
            }
        }

        binding.btnForgotPassWord.setOnClickListener {
            var isValid = true
            if (binding.txtEmailLogin.text.toString().isNullOrEmpty()) {
                binding.emailInputLayout.error = "Email cannot be empty"
                isValid = false
            } else if (!isValidEmail(binding.txtEmailLogin.text.toString())) {
                binding.emailInputLayout.error = "Please enter a valid email address"
                isValid = false
            } else {
                binding.emailInputLayout.error = null
            }
            if (isValid) {
                binding.progressCircularLogin.visibility = View.VISIBLE
                articleViewModel.forgotPassWord(binding.txtEmailLogin.text.toString())
            }
        }
    }


    private fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordPattern =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=!])(?=\\S+\$).{8,}\$"
        return password.isNotEmpty() && password.matches(passwordPattern.toRegex())
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