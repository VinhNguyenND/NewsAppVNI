package com.example.myappnews.Ui.Fragment.management.Author.EditRequest

import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.myappnews.Data.Firebase.ViewModel.AuthorViewModel.AuthorViewModel
import com.example.myappnews.Data.Model.Article.NewsArticle
import com.example.myappnews.Data.constant.dismissKeyboard
import com.example.myappnews.R
import com.example.myappnews.Ui.Fragment.Profile.byteToImage
import com.example.myappnews.Ui.Fragment.Profile.getImageBytesFromUri
import com.example.myappnews.Ui.Fragment.Profile.openCamera
import com.example.myappnews.Ui.Fragment.management.Author.Denied.imageViewToByteArray
import com.example.myappnews.Ui.Fragment.management.Author.Home.showToast
import com.example.myappnews.databinding.DeniedEditBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class EditRequest : Fragment() {
    private lateinit var binding: DeniedEditBinding
    private val _authorViewModel = AuthorViewModel.getInstance()
    private lateinit var article: NewsArticle;
    private var byteArrayImage = ByteArray(1000)
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
            binding.editTextSourceUrl.setText(article.sourceUrl.toString());
            binding.editTextSourceId.setText(article.sourceId.toString());
            binding.editTextCreator.setText(article.creator.toString());
            Glide.with(requireContext())
                .asBitmap()
                .load(article.imageUrl?.trim())
                .error(R.drawable.uploaderror)
                .fitCenter()
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        val outputStream = ByteArrayOutputStream()
                        resource.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                        val byteArray = outputStream.toByteArray()
                        byteArrayImage = byteArray;
                        binding.imageUpload.setImageBitmap(byteToImage(byteArray))
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        binding.imageUpload.setImageResource(R.drawable.uploaderror)
                        byteArrayImage = imageViewToByteArray(binding.imageUpload)
                    }
                })
        }
    }

    private fun event(view: View, newsArticle: NewsArticle) {
        binding.btnOk.setOnClickListener {
            dismissKeyboard(requireContext(),view)
            val newsArticle = NewsArticle(
                idArticle = newsArticle.idArticle, // ID article
                idPoster = newsArticle.idPoster,  // Thiếu trong binding
                idReviewer = newsArticle.idReviewer,  // Thiếu trong binding
                titleArticle = binding.editTextTitle.text.toString(), // Tiêu đề
                linkArticle = binding.editTextLink.text.toString(), // Link bài báo
                creator = binding.editTextCreator.text.toString(), // Tên tác giả
                content = binding.editTextContent.text.toString(), // Nội dung
                pubDate = article.pubDate, // Ngày xuất bản / URL ảnh
                sourceUrl = binding.editTextSourceUrl.text.toString(), // URL nguồn
                sourceId = binding.editTextSourceId.text.toString(), // ID nguồn
                country = binding.editTextCountry.text.toString(), // Quốc gia
                field = binding.editTextField.text.toString(), // Lĩnh vực
                isApprove = newsArticle.isApprove, // Trạng thái duyệt
                hide = newsArticle.hide, // Ẩn/Hiện
                requireEdit = 1, // Yêu cầu chỉnh sửa
                requiredDate = newsArticle.requiredDate,// Ngày yêu cầu chỉnh sửa
                cause = newsArticle.cause,
            )
            _authorViewModel.responseRqEdit(newsArticle, byteArrayImage)
                .observe(viewLifecycleOwner, Observer {
                    if (isAdded && isVisible) {
                        if (it == 1) {
                            showToast(requireContext(), "Gửi  yêu cầu thành công")
                            view.findNavController().popBackStack()
                        } else {
                            showToast(requireContext(), "Gửi  yêu cầu thất bại")
                        }
                    }
                })

        }
        binding.btnPutImage.setOnClickListener {
            ImagePicker.with(this)
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }
        binding.btnBack.setOnClickListener {
            Navigation.findNavController(binding.root).popBackStack()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            100 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera(requireActivity())
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Camera and Storage permission denied",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            if (resultCode == Activity.RESULT_OK) {
                val fileUri = data?.data
                showToast(requireContext(), fileUri.toString())
                if (fileUri != null) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val imageBytes: ByteArray? = getImageBytesFromUri(requireContext(), fileUri)
                        if (imageBytes != null) {
                            byteArrayImage = imageBytes
                        }
                    }
                    binding.imageUpload.setImageURI(fileUri)
                }
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                showToast(requireContext(), "up load fail")
            } else {
                Toast.makeText(requireContext(), "Image picker cancel", Toast.LENGTH_SHORT).show()
            }
        }
}