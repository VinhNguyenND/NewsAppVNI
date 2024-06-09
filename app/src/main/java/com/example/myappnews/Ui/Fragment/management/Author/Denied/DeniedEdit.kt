package com.example.myappnews.Ui.Fragment.management.Author.Denied

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.myappnews.Data.Firebase.ViewModel.AdminViewModel.AdminViewModel
import com.example.myappnews.Data.Firebase.ViewModel.ArticleViewModel.ArViewModel
import com.example.myappnews.Data.Firebase.ViewModel.AuthorViewModel.AuthorViewModel
import com.example.myappnews.Data.Model.Article.Field
import com.example.myappnews.Data.Model.Article.NewsArticle
import com.example.myappnews.Data.Model.Source.Source
import com.example.myappnews.Data.constant.dismissKeyboard
import com.example.myappnews.Interface.Adapter.CommonAdapter
import com.example.myappnews.R
import com.example.myappnews.Ui.Fragment.Home.Adapt.SourceAdapter
import com.example.myappnews.Ui.Fragment.Home.Adapt.popupAdapter
import com.example.myappnews.Ui.Fragment.Profile.byteToImage
import com.example.myappnews.Ui.Fragment.Profile.getImageBytesFromUri
import com.example.myappnews.Ui.Fragment.Profile.openCamera
import com.example.myappnews.Ui.Fragment.management.Author.Home.showToast
import com.example.myappnews.databinding.DeniedEditBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.Date

class DeniedEdit : Fragment() {
    private lateinit var binding: DeniedEditBinding
    private val _authorViewModel = AuthorViewModel.getInstance()
    private val _adminViewModel = AdminViewModel.getInstance()
    private val ArticleViewModel = ArViewModel.getInstance();
    private var listField = ArrayList<Field>()
    private var listSource = ArrayList<Source>()
    private var positionText = 0
    private lateinit var adapter: popupAdapter
    private lateinit var sourceAdapter: SourceAdapter
    private lateinit var article: NewsArticle;
    private var make = 0;
    private lateinit var idDoc: String
    private var byteArrayImage = ByteArray(100000000)
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
        initAdapter()
        initContent(view);
        event(view, article)
    }

    override fun onPause() {
        super.onPause()
        _authorViewModel.setSendArticleEdit(null)
    }

    override fun onResume() {
        super.onResume()
        getAllField()
        getAllSource()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun initContent(view: View) {
        _authorViewModel.observerArticleEdit().observeForever {
            if (isAdded && isVisible) {
                if (it == true) {
                    showToast(requireContext(), "Gửi yêu cầu thành công")
                    binding.progreesCircle.visibility = View.GONE
                    binding.body.isEnabled = true
                    view.findNavController().popBackStack()
                    view.findNavController().popBackStack()
                } else if (it == false) {
                    binding.progreesCircle.visibility = View.GONE
                    binding.body.isEnabled = true
                    showToast(requireContext(), "Gửi yêu cầu thất bại")
                }
            }
        }
        if (arguments?.getParcelable("Article", NewsArticle::class.java) != null) {
            article = arguments?.getParcelable("Article", NewsArticle::class.java)!!
            val text: String = article.content!!.replace("\\\\n", "<br/>" + " ");
            binding.editTextTitle.setText(article.titleArticle.toString());
            binding.editTextContent.setText(article.content.toString());
            binding.editTextCountry.setText(article.country.toString());
            binding.editTextLink.setText(article.linkArticle.toString());
            binding.editTextField.setText(article.field.toString());
            binding.editTextSourceUrl.setText(article.sourceUrl.toString());
            binding.editTextSourceId.setText(article.sourceId).toString();
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
        _adminViewModel.getIdDoc(article.idArticle!!.trim())
            .observe(viewLifecycleOwner, Observer {
                idDoc = it;
            })
    }

    private fun getAllField() {
        ArticleViewModel.getAllField().observe(
            viewLifecycleOwner, Observer {
                listField = it;
                listField.removeAt(0)
                adapter.submitList(listField);
            }
        )

    }

    private fun getAllSource() {
        ArticleViewModel.getAllSource().observe(viewLifecycleOwner,
            Observer {
                listSource = it;
                listSource.removeAt(0)
                sourceAdapter.submitList(listSource)
            })
    }

    private fun initAdapter() {
        adapter = popupAdapter(listField, requireContext())
        sourceAdapter = SourceAdapter(listSource, requireContext())
    }

    private fun showCustomDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.all_pop_up_window)
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
        val recyclerView = dialog.findViewById<RecyclerView>(R.id.allchoosepopuprc)
        dialog.findViewById<TextView>(R.id.btncloseallpop).setOnClickListener {
            dialog.cancel()
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter.setClickListener(object : CommonAdapter {
            @SuppressLint("SetTextI18n")
            override fun setOnClickListener(position: Int) {
                for (i in listField.indices) {
                    if (i != position) {
                        listField[i].choose = false
                    }
                }
                binding.editTextField.text = listField[position].fieldId.toString()
                listField[position].choose = !listField[position].choose;
                adapter.submitList(listField);
                dialog.dismiss()
            }
        })
        recyclerView.adapter = adapter
        dialog.show()
    }

    private fun showCustomDialogSource() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.all_pop_up_window)
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
        val recyclerView = dialog.findViewById<RecyclerView>(R.id.allchoosepopuprc)
        dialog.findViewById<TextView>(R.id.btncloseallpop).setOnClickListener {
            dialog.cancel()
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        sourceAdapter.setClickListener(object : CommonAdapter {
            @SuppressLint("SetTextI18n")
            override fun setOnClickListener(position: Int) {
                for (i in listSource.indices) {
                    if (i != position) {
                        listSource[i].choose = false
                    }
                }
                binding.editTextSourceUrl.text = listSource[position].SourceName
                listSource[position].choose = !listSource[position].choose;
                sourceAdapter.submitList(listSource);
                dialog.dismiss()
            }
        })
        recyclerView.adapter = sourceAdapter
        dialog.show()
    }

    private fun format(): Boolean {
        var isValid = true;
        if (TextUtils.isEmpty(binding.editTextTitle.getText().toString().trim())) {
            binding.editTextTitle.setError("Vui lòng nhập tiêu đề");
            isValid = false;
            Log.d("binding.editTextTitle", isValid.toString())
        } else if (binding.editTextTitle.getText().toString().trim().length < 25) {
            binding.editTextTitle.setError("Tiêu đề phải có ít nhất 25 ký tự");
            isValid = false;
            Log.d("binding.editTextTitle", isValid.toString())
        }

        if (binding.editTextLink.text.toString().trim().isEmpty()) {
            binding.editTextLink.error = "Vui lòng nhập link"
            isValid = false
            Log.d("binding.editTextLink", isValid.toString())
        }
//        else if (!isValidUrl(binding.editTextLink.text.toString().trim())) {
//            binding.editTextLink.error = "Vui lòng nhập một URL hợp lệ"
//            isValid = false
//        }

        if (TextUtils.isEmpty(binding.editTextCreator.getText().toString().trim())) {
            binding.editTextCreator.setError("Vui lòng nhập tác giả");
            isValid = false;
            Log.d("binding.editTextCreator", isValid.toString())
        } else if (binding.editTextCreator.getText().toString().trim().length < 5) {
            binding.editTextCreator.setError("Tác giả phải có ít nhất 25 ký tự");
            isValid = false;
            Log.d("binding.editTextCreator", isValid.toString())
        }


        if (TextUtils.isEmpty(binding.editTextContent.getText().toString().trim())) {
            binding.editTextContent.setError("Vui lòng nhập nội dung");
            isValid = false;
            Log.d("binding.editTextContent", isValid.toString())
        } else if (binding.editTextContent.getText().toString().trim().length < 255) {
            binding.editTextContent.setError("Nội dung phải có ít nhất 255 ký tự");
            isValid = false;
            Log.d("binding.editTextContent", isValid.toString())
        }

        if (TextUtils.isEmpty(binding.editTextSourceUrl.getText().toString().trim())) {
            binding.editTextSourceUrl.setError("Vui lòng nhập URL nguồn");
            isValid = false;
            Log.d("binding.editTextSourceUrl", isValid.toString())
        }
//        else if (!isValidUrl(binding.editTextSourceUrl.text.toString().trim())) {
//            binding.editTextLink.error = "Vui lòng nhập một URL hợp lệ"
//            isValid = false
//        }

//        if (TextUtils.isEmpty(binding.editTextSourceId.getText().toString().trim())) {
//            binding.editTextSourceId.setError("Vui lòng nhập tên nguồn");
//            isValid = false;
//            Log.d("binding.editTextSourceId", isValid.toString())
//        } else if (binding.editTextSourceId.getText().toString().trim().length < 5) {
//            binding.editTextSourceId.setError("Tên nguồn phải có ít nhất 25 ký tự");
//            isValid = false;
//            Log.d("binding.editTextSourceId", isValid.toString())
//        }

        return isValid
    }


    private fun event(view: View, newsArticle: NewsArticle) {
        binding.DropDown.setOnClickListener {
            showCustomDialog()
        }
        binding.DropDownSource.setOnClickListener {
            showCustomDialogSource()
        }
        binding.btnOk.setOnClickListener {
            if (format()) {
                binding.progreesCircle.visibility = View.VISIBLE
                val newsArticle = NewsArticle(
                    idArticle = newsArticle.idArticle, // ID article
                    idPoster = newsArticle.idPoster,  // Thiếu trong binding
                    idReviewer = newsArticle.idReviewer,  // Thiếu trong binding
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
                    hide = newsArticle.hide, // Ẩn/Hiện
                    requireEdit = newsArticle.requireEdit, // Yêu cầu chỉnh sửa
                    requiredDate = newsArticle.requiredDate// Ngày yêu cầu chỉnh sửa
                )
                _authorViewModel.sendArticleEdit(newsArticle, byteArrayImage)
            }
        }
        binding.btnBack.setOnClickListener {
            view.findNavController().popBackStack()
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

fun imageViewToByteArray(imageView: ImageView): ByteArray {

    val bitmap: Bitmap = (imageView.drawable).toBitmap()

    // Chuyển đổi Bitmap thành mảng byte
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    return outputStream.toByteArray()
}
