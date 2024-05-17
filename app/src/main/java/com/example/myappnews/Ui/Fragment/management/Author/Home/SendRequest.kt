package com.example.myappnews.Ui.Fragment.management.Author.Home

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.example.myappnews.Data.Firebase.ViewModel.AdminViewModel.AdminViewModel
import com.example.myappnews.Data.Firebase.ViewModel.AuthorViewModel.AuthorViewModel
import com.example.myappnews.Data.Model.Article.NewsArticle
import com.example.myappnews.Data.SharedPreferences.Shared_Preference
import com.example.myappnews.R
import com.example.myappnews.Ui.Fragment.Home.Adapt.ArticleAdapter
import com.example.myappnews.Ui.Fragment.Profile.byteToImage
import com.example.myappnews.Ui.Fragment.Profile.getImageBytesFromUri
import com.example.myappnews.Ui.Fragment.Profile.openCamera
import com.example.myappnews.Ui.Fragment.management.Author.Denied.imageViewToByteArray
import com.example.myappnews.databinding.SendRequestBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.IOException
import java.io.InputStream
import java.security.MessageDigest
import java.time.LocalDateTime
import java.util.Date

class SendRequest : Fragment() {
    private lateinit var binding: SendRequestBinding
    private val _authorViewModel = AuthorViewModel.getInstance()
    private val _adminViewModel = AdminViewModel.getInstance()
    private lateinit var _articleAdapter: ArticleAdapter
    private var listArticle = ArrayList<NewsArticle>()
    private var byteArrayImage = ByteArray(1000);
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
        byteArrayImage = imageViewToByteArray(binding.imageUpload)
        event(view)
    }


    private fun event(view: View) {
        binding.btnOk.setOnClickListener {
            var id = sha256(LocalDateTime.now().toString());
            val newsArticle = NewsArticle(
                idArticle = id, // ID article
                idPoster = _shared_Preference.getUid().toString(),  // Thiếu trong binding
                idReviewer = null,  // Thiếu trong binding
                titleArticle = binding.editTextTitle.text.toString(), // Tiêu đề
                linkArticle = binding.editTextLink.text.toString(), // Link bài báo
                creator = binding.editTextCreator.text.toString(), // Tên tác giả
                content = binding.editTextContent.text.toString(), // Nội dung
                pubDate = null, // Ngày xuất bản // URL ảnh
                sourceUrl = binding.editTextSourceUrl.text.toString(), // URL nguồn
                sourceId = binding.editTextSourceId.text.toString(), // ID nguồn
                country = binding.editTextCountry.text.toString(), // Quốc gia
                field = binding.editTextField.text.toString(), // Lĩnh vực
                isApprove = 0, // Trạng thái duyệt
                hide = true, // Ẩn/Hiện
                requireEdit = null, // Yêu cầu chỉnh sửa
                requiredDate = Date() // Ngày yêu cầu chỉnh sửa
            )
            _authorViewModel.sendArticleEdit(newsArticle, byteArrayImage).observeForever {
                if (isAdded && isVisible) {
                    if (it == true) {
                        showToast(requireContext(), "Gửi yêu cầu thành công")
                    } else {
                        showToast(requireContext(), "Gửi yêu cầu thất bại")
                    }
                }
            }
        };
        binding.uploadContent.setOnClickListener {
            chooseWordFile(requireContext());
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

    private fun readWordFile(uri: Uri) {
        try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            inputStream?.use { stream ->
                val document = XWPFDocument(stream)
                val paragraphs = document.paragraphs
                val stringBuilder = StringBuilder()
                for (paragraph in paragraphs) {
                    stringBuilder.append(paragraph.text).append("\n")
                }
                val content = stringBuilder.toString()
                binding.editTextContent.setText(content)
            }
        } catch (e: Exception) {
            Log.d("lỗi dọc file", e.toString());
            showToast(requireContext(), e.toString())
            e.printStackTrace()
        }
    }
//    private fun readWordFile(filePath: Uri): String {
//        var content = ""
//        try {
//            val inputStream: InputStream =  requireContext().contentResolver.openInputStream(uri) ?: return ""
//            val handler = BodyContentHandler()
//            val metadata = Metadata()
//            val context = ParseContext()
//            val parser = AutoDetectParser()
//            parser.parse(inputStream, handler, metadata, context)
//        } catch (e: IOException) {
//            // Xử lý lỗi khi có vấn đề với tệp tin hoặc IO
//            e.printStackTrace()
//        } catch (e: Exception) {
//            // Xử lý lỗi chung
//            e.printStackTrace()
//        }
//        return content
//    }

    private val pickWordFileLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                data?.data?.let { uri ->
                    readWordFile(uri)
                }
            }
        }


    private fun chooseWordFile(context: Context) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type =
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document" // MIME type for .docx files
            putExtra(
                Intent.EXTRA_MIME_TYPES,
                arrayOf(
                    "application/msword",
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                )
            )
        }
        try {
            pickWordFileLauncher.launch(intent)
        } catch (ex: ActivityNotFoundException) {
            // Xử lý trường hợp không tìm thấy ứng dụng để xử lý Intent
            showToast(context, "not found  app have file");
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
