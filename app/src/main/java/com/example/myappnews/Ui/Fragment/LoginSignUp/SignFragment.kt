package com.example.myappnews.Ui.Fragment.LoginSignUp

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
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
import com.example.myappnews.Data.Model.User.UserModel
import com.example.myappnews.Data.constant.dismissKeyboard
import com.example.myappnews.R
import com.example.myappnews.Ui.Fragment.management.Author.Home.showToast
import com.example.myappnews.databinding.SignupScreenBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class SignFragment : Fragment() {
    private lateinit var binding: SignupScreenBinding;
    private val articleViewModel = ArViewModel.getInstance()
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SignupScreenBinding.inflate(inflater, container, false);
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        initFireBase()
        setOnclick(view)
    }

    private fun initFireBase() {
        auth = Firebase.auth
    }

    private fun observer() {
        articleViewModel.OberverSignIn().observe(
            viewLifecycleOwner, Observer {
                if (it == 1) {
                    showToast(requireContext(), "Đăng ký thành công")
                    val nav = Navigation.findNavController(binding.root)
                    nav.popBackStack(
                        nav.graph.startDestinationId,
                        false
                    )
                }
            }
        )
    }

    private fun SignUp(view: View,Email: String, PassWord: String, name: String) {
         dismissKeyboard(requireContext(),view)
        var isValid = true
        val email = binding.SignEmailText.text.toString().trim()
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailInputLayout.error = "Email không hợp lệ"
            isValid = false
        } else {
            binding.emailInputLayout.error = null
        }

        val name1 = binding.NameSignText.text.toString().trim()
        if (name1.isEmpty()) {
            binding.nameInputLayout.error = "Tên không được để trống"
            isValid = false
        } else {
            binding.nameInputLayout.error = null
        }

        val password = binding.PassWordSignText.text.toString().trim()
        if (!isValidPassword(password)) {
            binding.passwordInputLayout.error =
                "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one number, and one special character"
        } else {
            binding.passwordInputLayout.error = null
        }

        val confirmPassword = binding.PassWordSignTextAgain.text.toString().trim()
        if (confirmPassword.isEmpty() || confirmPassword != password) {
            binding.confirmPasswordInputLayout.error = "Mật khẩu xác nhận không khớp"
            isValid = false
        } else {
            binding.confirmPasswordInputLayout.error = null
        }

        if (isValid) {
            binding.signProgress.visibility = View.VISIBLE;
            auth.createUserWithEmailAndPassword(Email, PassWord)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val idUser = auth.currentUser?.uid
                        val userModel = UserModel(idUser, Email, PassWord, name, "", "User")
                        db.collection("Users")
                            .add(userModel.toMap())
                            .addOnCompleteListener {
                                binding.signProgress.visibility = View.INVISIBLE;
                                if (it.isSuccessful) {
                                    articleViewModel.SignIn(requireActivity(), Email, PassWord)
                                } else {
                                    showToast(requireContext(), "Sign up fail");
                                }
                            }
                            .addOnFailureListener {
                                binding.signProgress.visibility = View.INVISIBLE;
                                showToast(requireContext(), "Sign up fail")
                                auth.currentUser?.delete()
                            }
                    } else {
                        binding.signProgress.visibility = View.INVISIBLE;
                        showToast(requireContext(), "Sign up fail")
                    }
                }
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

    fun setOnclick(view: View) {
        binding.SignButton.setOnClickListener {
            val email = binding.SignEmailText.text.toString()
            val name = binding.NameSignText.text.toString()
            val passWord = binding.PassWordSignText.text.toString()
            if (email != null && name != null) {
                SignUp(view,email, passWord, name);
            }
        }
        binding.toLogin.setOnClickListener {
            Navigation.findNavController(binding.root).popBackStack()
        }
    }
}

private fun isValidPassword(password: String): Boolean {
    val passwordPattern =
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=!])(?=\\S+\$).{8,}\$"
    return password.isNotEmpty() && password.matches(passwordPattern.toRegex())
}

private fun isValidEmail(email: String): Boolean {
    return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
}