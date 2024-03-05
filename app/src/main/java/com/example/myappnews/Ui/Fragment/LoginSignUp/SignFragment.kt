package com.example.myappnews.Ui.Fragment.LoginSignUp

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myappnews.databinding.SignupScreenBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class SignFragment : Fragment() {
    private lateinit var binding: SignupScreenBinding;
    private lateinit var auth: FirebaseAuth
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
    }

    private fun initFireBase() {
        auth = Firebase.auth
    }

    private fun SignUp(Email: String, PassWord: String) {
        binding.signProgress.visibility = View.VISIBLE;
        auth.createUserWithEmailAndPassword(Email, PassWord)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser?.uid
                    binding.signProgress.visibility = View.INVISIBLE;
                    Toast.makeText(requireContext(), user.toString(), Toast.LENGTH_LONG).show();
                } else {
                    // If sign in fails, display a message to the user.
                    binding.signProgress.visibility = View.INVISIBLE;
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        requireContext(),
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()

                }
            }
    }

    private fun setOnclick() {
        binding.SignButton.setOnClickListener {
            val email = binding.SignEmailText.text.toString()
            val name = binding.NameSignText.text.toString()
            val passWord = binding.PassWordSignText.text.toString()
            if (email != null && name != null) {
                SignUp(email, passWord);
            }
        }
    }
}