package com.example.myappnews.Ui.Fragment.Profile

import android.annotation.SuppressLint
import android.app.Activity
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
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.myappnews.Data.Enum.UserEnum
import com.example.myappnews.Data.Firebase.ViewModel.ArticleViewModel.ArViewModel
import com.example.myappnews.Data.SharedPreferences.Shared_Preference
import com.example.myappnews.R
import com.example.myappnews.databinding.ProfileScreenBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.util.Date

class Profile_Fragment : Fragment() {
    private lateinit var binding: ProfileScreenBinding
    private lateinit var _shared_Preference: Shared_Preference;
    private var auth: FirebaseAuth = Firebase.auth
    private val ArticleViewModel = ArViewModel.getInstance();
    private val CAMERA_REQUEST_CODE = 100
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ProfileScreenBinding.inflate(inflater, container, false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _shared_Preference = Shared_Preference(requireActivity());
        ObserveProfile()
        event();
    }

    override fun onResume() {
        initLayout()
        super.onResume()
    }


    private fun navigate(id: Int) {
        Navigation.findNavController(binding.root).navigate(id)
    }

    private fun event() {
        binding.addMin.setOnClickListener {
            when (_shared_Preference.getPermission()) {
                UserEnum.Admin.toString() -> {
                    navigate(R.id.mainAdmFrag)
                }

                UserEnum.User.toString() -> {
                    binding.layoutSpecial.visibility = View.VISIBLE
                }

                UserEnum.Author.toString() -> {
                    navigate(R.id.authorHome)

                }

                UserEnum.Authorized.toString() -> {
                    binding.layoutSpecial.visibility = View.VISIBLE
                }
            }
        }
        binding.DangNhapProfile.setOnClickListener {
            navigate(R.id.loginFragment)
        }
        binding.btnDangXuat.setOnClickListener {
            _shared_Preference.logout()
            auth.signOut()
            binding.layoutSpecial.visibility = View.INVISIBLE
            binding.DangNhapProfile.visibility = View.VISIBLE
        }
        binding.imagePicker1.setOnClickListener {
            ImagePicker.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )    //Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }
    }

    @SuppressLint("CheckResult")
    private fun ObserveProfile() {
        _shared_Preference.getUid()?.let {
            ArticleViewModel.getUser(it).observe(
                viewLifecycleOwner,
                Observer {
                    Glide.with(requireContext())
                        .load(it.Image)
                        .error(R.drawable.none_avatar)
                        .fitCenter()
                        .into(binding.avatarProfile)
                }
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == ImagePicker.REQUEST_CODE) {
            val fileUri = data?.data
            showToast(requireContext(), fileUri.toString())
            if (fileUri != null) {
                lifecycleScope.launch(Dispatchers.IO) {
                    val imageBytes: ByteArray? = getImageBytesFromUri(requireContext(), fileUri)
                    Log.d("anh", imageBytes.toString())
                    if (imageBytes != null) {
                        _shared_Preference.getUid()
                            ?.let {
                                ArticleViewModel.setImage(
                                    imageBytes,
                                    it
                                )
                            }
                    }
                }
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            showToast(requireContext(), "up load fail")
        } else {
            Toast.makeText(requireContext(), "Image picker cancel", Toast.LENGTH_SHORT).show()
        }
    }

    fun uriToByteArray(uri: String): ByteArray? {
        var inputStream: FileInputStream? = null
        try {
            inputStream = FileInputStream(uri)
            val buffer = ByteArrayOutputStream()
            val bufferSize = 1024
            val bufferArray = ByteArray(bufferSize)
            var bytesRead: Int
            while (inputStream.read(bufferArray).also { bytesRead = it } != -1) {
                buffer.write(bufferArray, 0, bytesRead)
            }
            return buffer.toByteArray()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            inputStream?.close()
        }
        return null
    }

    suspend fun getImageBytesFromUri(context: Context, uri: Uri): ByteArray? {
        return withContext(Dispatchers.IO) {
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                inputStream?.readBytes()
            } catch (e: Exception) {
                e.printStackTrace()
                null
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
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Quyền truy cập camera và bộ nhớ được cấp, bạn có thể sử dụng camera ở đây
                    openCamera()
                } else {
                    // Quyền truy cập camera và bộ nhớ không được cấp, thông báo cho người dùng
                    Toast.makeText(
                        requireContext(),
                        "Camera and Storage permission denied",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun openCamera() {
        ImagePicker.with(this)
            .cameraOnly()
            .crop()
            .start()
    }

    private fun initLayout() {
        if (_shared_Preference.isLogin()) {
            binding.UserNamPr.text = _shared_Preference.getName()
            binding.UserEmailPr.text = _shared_Preference.getEmail()
            when (_shared_Preference.getPermission()) {
                UserEnum.Admin.toString() -> {
                    binding.layoutSpecial.visibility = View.VISIBLE
                }

                UserEnum.User.toString() -> {
                    binding.layoutSpecial.visibility = View.VISIBLE
                    binding.addMin.visibility = View.GONE
                }

                UserEnum.Author.toString() -> {
                    binding.layoutSpecial.visibility = View.VISIBLE
                }

                UserEnum.Authorized.toString() -> {
                    binding.layoutSpecial.visibility = View.VISIBLE
                }
            }
        } else {
            binding.DangNhapProfile.visibility = View.VISIBLE
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
}