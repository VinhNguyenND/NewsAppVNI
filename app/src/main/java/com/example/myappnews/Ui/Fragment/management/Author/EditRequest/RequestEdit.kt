package com.example.myappnews.Ui.Fragment.management.Author.EditRequest

import android.app.Dialog
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.myappnews.Data.Firebase.ViewModel.AdminViewModel.AdminViewModel
import com.example.myappnews.Data.Firebase.ViewModel.AuthorViewModel.AuthorViewModel
import com.example.myappnews.Data.Model.Article.NewsArticle
import com.example.myappnews.R
import com.example.myappnews.Ui.Fragment.Profile.imageViewToByteArray
import com.example.myappnews.Ui.Fragment.management.Author.Home.showToast
import com.example.myappnews.databinding.EditRequestBinding
import java.io.ByteArrayOutputStream

class RequestEdit : Fragment() {
    private lateinit var binding: EditRequestBinding
    private lateinit var article: NewsArticle;
    private val _adminViewModel = AdminViewModel.getInstance()
    private val _authorViewModel = AuthorViewModel.getInstance()
    private lateinit var idDoc: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = EditRequestBinding.inflate(inflater, container, false);
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initContent()
        event(view);
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun initContent() {
        if (arguments?.getParcelable("Article", NewsArticle::class.java) != null) {
            article = arguments?.getParcelable("Article", NewsArticle::class.java)!!
            val text: String = article.content!!.replace("\\\\n", "<br/>" + " ");
            binding.txtPageContent.text = Html.fromHtml(text)
            binding.causeEdit.text = article.cause
            Glide.with(requireContext())
                .load(article.imageUrl?.trim())
                .error(R.drawable.uploaderror)
                .fitCenter()
                .into(binding.imgArticlePage)
            _adminViewModel.getIdDoc(article.idArticle!!.trim())
                .observe(viewLifecycleOwner, Observer {
                    idDoc = it;
                })
        }
    }

    private fun event(view: View) {
        binding.btnbackar.setOnClickListener {
            view.findNavController().popBackStack();
        }
        binding.btnEditAtuhor.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("Article", article)
            view.findNavController().navigate(R.id.editRequest, bundle)
        }
        binding.btnDenied.setOnClickListener {
            showCustomDialog()
        }
    }

    private fun showCustomDialog() {
        val dialog = Dialog(requireContext())
        dialog.setCancelable(false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.approve_popup)
        val window: Window? = dialog.window;
        if (window == null) {
            return
        }
        window.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        val windowAtribute: WindowManager.LayoutParams = window.attributes
        windowAtribute.gravity = Gravity.CENTER
        window.attributes = windowAtribute
        val ok = dialog.findViewById<Button>(R.id.btnDongy)
        val cancel = dialog.findViewById<Button>(R.id.btnLoaiBo)
        val text = dialog.findViewById<TextView>(R.id.textView)
        text.text = "Bạn có chắc muốn từ chối yêu cầu"
        ok.setOnClickListener {
            val newsArticle = NewsArticle(
                idArticle = article.idArticle, // ID article
                idPoster = article.idPoster,  // Thiếu trong binding
                idReviewer = article.idReviewer,  // Thiếu trong binding
                titleArticle = article.titleArticle, // Tiêu đề
                linkArticle = article.linkArticle, // Link bài báo
                creator = article.creator, // Tên tác giả
                content = article.content, // Nội dung
                pubDate = article.pubDate, // Ngày xuất bản
                imageUrl = article.imageUrl, // URL ảnh
                sourceUrl = article.sourceUrl, // URL nguồn
                sourceId = article.sourceId, // ID nguồn
                country = article.country, // Quốc gia
                field = article.field, // Lĩnh vực
                isApprove = article.isApprove, // Trạng thái duyệt
                hide = article.hide, // Ẩn/Hiện
                requireEdit = -1, // Yêu cầu chỉnh sửa
                requiredDate = article.requiredDate,// Ngày yêu cầu chỉnh sửa
                cause = article.cause,
            )
            _authorViewModel.responseRqEdit(
                newsArticle,
                imageToByteArray(binding.imgArticlePage)
            )
                .observe(viewLifecycleOwner, Observer {
                    showToast(requireContext(), "Gửi  yêu cầu thành công")
                })
            dialog.dismiss()
        }
        cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }


}

fun imageToByteArray(imageView: ImageView): ByteArray {

    val bitmap: Bitmap = (imageView.drawable).toBitmap()

    // Chuyển đổi Bitmap thành mảng byte
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    return outputStream.toByteArray()
}