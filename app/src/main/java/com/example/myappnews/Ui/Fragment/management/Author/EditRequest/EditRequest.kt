package com.example.myappnews.Ui.Fragment.management.Author.EditRequest

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.myappnews.Data.Firebase.ViewModel.AuthorViewModel.AuthorViewModel
import com.example.myappnews.Data.Model.Article.NewsArticle
import com.example.myappnews.Ui.Fragment.management.Author.Home.showToast
import com.example.myappnews.databinding.DeniedEditBinding

class EditRequest : Fragment() {
    private lateinit var binding: DeniedEditBinding
    private val _authorViewModel = AuthorViewModel.getInstance()
    private lateinit var article: NewsArticle;
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DeniedEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initContent();
        event(view, article)
        Log.i("du lieu chuyen sang", article.toString());
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun initContent() {
        if (arguments?.getParcelable("Article", NewsArticle::class.java) != null) {
            article = arguments?.getParcelable("Article", NewsArticle::class.java)!!
            val text: String = article.content!!.replace("\\\\n", "<br/>" + " ");
            binding.editTextTitle.setText(article.titleArticle.toString());
            binding.editTextContent.setText(article.content.toString());
            binding.editTextCountry.setText(article.country.toString());
            binding.editTextLink.setText(article.linkArticle.toString());
            binding.editTextField.setText(article.field.toString());
            binding.editTextImageUrl.setText(article.imageUrl.toString());
            binding.editTextSourceUrl.setText(article.sourceUrl.toString());
            binding.editTextSourceId.setText(article.sourceId.toString());
            binding.editTextCreator.setText(article.creator.toString());
        }
    }

    private fun event(view: View, newsArticle: NewsArticle) {
        binding.btnOk.setOnClickListener {
            val newsArticle = NewsArticle(
                idArticle = newsArticle.idArticle, // ID article
                idPoster = newsArticle.idPoster,  // Thiếu trong binding
                idReviewer = newsArticle.idReviewer,  // Thiếu trong binding
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
                isApprove = newsArticle.isApprove, // Trạng thái duyệt
                hide = newsArticle.hide, // Ẩn/Hiện
                requireEdit = -1, // Yêu cầu chỉnh sửa
                requiredDate = newsArticle.requiredDate,// Ngày yêu cầu chỉnh sửa
                cause = newsArticle.cause,
            )
            _authorViewModel.responseRqEdit(newsArticle)
                .observe(viewLifecycleOwner, Observer {
                    showToast(requireContext(),"Gửi  yêu cầu thành công")
                })
//            Log.d("du lieu chuyen sang", article.toString());
        }
    }
}