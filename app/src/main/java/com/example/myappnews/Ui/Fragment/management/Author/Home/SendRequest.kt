package com.example.myappnews.Ui.Fragment.management.Author.Home

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myappnews.Data.Firebase.ViewModel.AdminViewModel.AdminViewModel
import com.example.myappnews.Data.Firebase.ViewModel.AuthorViewModel.AuthorViewModel
import com.example.myappnews.Data.Model.Article.NewsArticle
import com.example.myappnews.Data.SharedPreferences.Shared_Preference
import com.example.myappnews.R
import com.example.myappnews.Ui.Fragment.Home.Adapt.ArticleAdapter
import com.example.myappnews.databinding.SendRequestBinding
import java.security.MessageDigest
import java.time.LocalDateTime
import java.util.Date

class SendRequest : Fragment() {
    private lateinit var binding: SendRequestBinding
    private val _authorViewModel = AuthorViewModel.getInstance()
    private val _adminViewModel = AdminViewModel.getInstance()
    private lateinit var _articleAdapter: ArticleAdapter
    private var listArticle = ArrayList<NewsArticle>()
    private lateinit var _shared_Preference: Shared_Preference;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SendRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _shared_Preference = Shared_Preference(requireActivity());
        event(view)
    }


    private fun event(view: View) {
        binding.btnOk.setOnClickListener {
            val newsArticle = NewsArticle(
                idArticle = sha256(LocalDateTime.now().toString()), // ID article
                idPoster = _shared_Preference.getUid().toString(),  // Thiếu trong binding
                idReviewer = null,  // Thiếu trong binding
                titleArticle = binding.editTextTitle.text.toString(), // Tiêu đề
                linkArticle = binding.editTextLink.text.toString(), // Link bài báo
                creator = binding.editTextCreator.text.toString(), // Tên tác giả
                content = binding.editTextContent.text.toString(), // Nội dung
                pubDate = null, // Ngày xuất bản
                imageUrl = binding.editTextImageUrl.text.toString(), // URL ảnh
                sourceUrl = binding.editTextSourceUrl.text.toString(), // URL nguồn
                sourceId = binding.editTextSourceId.text.toString(), // ID nguồn
                country = binding.editTextCountry.text.toString(), // Quốc gia
                field = binding.editTextField.text.toString(), // Lĩnh vực
                isApprove = 0, // Trạng thái duyệt
                hide = true, // Ẩn/Hiện
                requireEdit = null, // Yêu cầu chỉnh sửa
                requiredDate = Date() // Ngày yêu cầu chỉnh sửa
            )
            _authorViewModel.sendRequest(sha256(LocalDateTime.now().toString()),newsArticle).observeForever {
                if (it == true) {
                    showToast(requireContext(), "Gửi yêu cầu thành công")
                } else {
                    showToast(requireContext(), "Gửi yêu cầu thất bại")
                }
            }
            showToast(requireContext(), "home");
        }
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