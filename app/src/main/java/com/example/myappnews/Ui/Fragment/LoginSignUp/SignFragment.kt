package com.example.myappnews.Ui.Fragment.LoginSignUp

import android.content.ContentValues.TAG
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
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.example.myappnews.Data.Firebase.ViewModel.ArticleViewModel.ArViewModel
import com.example.myappnews.Data.Model.User.UserModel
import com.example.myappnews.R
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
        initFireBase()
        setOnclick()
        showToast(requireContext(), auth.currentUser?.uid.toString())
    }

    private fun initFireBase() {
        auth = Firebase.auth
    }

    private fun SignUp(Email: String, PassWord: String, name: String) {
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
                                articleViewModel.SignIn(requireActivity(), Email, PassWord).observe(
                                    viewLifecycleOwner, Observer {
                                        val nav = Navigation.findNavController(binding.root)
                                        nav.popBackStack(nav.graph.startDestinationId, false)
                                    }
                                )
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

    private fun setOnclick() {
        binding.SignButton.setOnClickListener {
            val email = binding.SignEmailText.text.toString()
            val name = binding.NameSignText.text.toString()
            val passWord = binding.PassWordSignText.text.toString()
            if (email != null && name != null) {
                SignUp(email, passWord, name);
            }
        }
        binding.toLogin.setOnClickListener {
            Navigation.findNavController(binding.root).popBackStack()
        }
    }


}