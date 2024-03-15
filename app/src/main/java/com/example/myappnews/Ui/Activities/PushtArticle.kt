package com.example.myappnews.Ui.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.myappnews.R
import com.example.myappnews.databinding.ActivityPushtArticleBinding
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.security.MessageDigest
import java.time.LocalDateTime


class PushtArticle : AppCompatActivity() {
    private val db = Firebase.firestore
    private lateinit var binding: ActivityPushtArticleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPushtArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnOk.setOnClickListener {
            pushArticle()
            binding.editTextField.text=null;
            binding.editTextCreator.text=null;
            binding.editTextCountry.text=null;
            binding.editTextSourceId.text=null;
            binding.editTextSourceUrl.text=null;
            binding.editTextContent.text=null;
            binding.editTextImageUrl.text=null;
            binding.editTextLink.text=null;
            binding.editTextField.text=null;
            binding.editTextPubDate.text=null;
            binding.editTextTitle.text=null;
        }
    }

    fun pushArticle() {
        val article = hashMapOf(
            "idArticle" to sha256(LocalDateTime.now().toString()),
            "titleArticle" to binding.editTextTitle.text.toString(),
            "linkArticle" to binding.editTextLink.text.toString(),
            "creator" to binding.editTextCreator.text.toString(),
            "content" to binding.editTextContent.text.toString(),
            "pubDate" to FieldValue.serverTimestamp(),
            "imageUrl" to binding.editTextImageUrl.text.toString(),
            "sourceUrl" to binding.editTextSourceUrl.text.toString(),
            "sourceId" to binding.editTextSourceId.text.toString(),
            "country" to binding.editTextCountry.text.toString(),
            "field" to binding.editTextField.text.toString(),
        )
        db.collection("Articles")
            .add(article)
            .addOnCompleteListener {
                Toast.makeText(this, "thanh cong", Toast.LENGTH_LONG).show()
            }.addOnFailureListener {
                Toast.makeText(this, "that bai", Toast.LENGTH_LONG).show()
            }
    }

    fun sha256(input: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(input.toByteArray())
        val hashString = StringBuilder()

        for (byte in hashBytes) {
            hashString.append(String.format("%02x", byte))
        }

        return hashString.toString()
    }
}