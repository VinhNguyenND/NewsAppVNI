package com.example.myappnews.Ui.Fragment.Profile

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.myappnews.Data.Api.Internet.InternetViewModel
import com.example.myappnews.Data.Enum.UserEnum
import com.example.myappnews.Data.Firebase.ViewModel.ArticleViewModel.ArViewModel
import com.example.myappnews.Data.SharedPreferences.Shared_Preference
import com.example.myappnews.R
import com.example.myappnews.databinding.ProfileScreenBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream

class Profile_Fragment : Fragment() {
    private lateinit var binding: ProfileScreenBinding
    private lateinit var internetViewModel: InternetViewModel
    private lateinit var _shared_Preference: Shared_Preference;
    private var auth: FirebaseAuth = Firebase.auth
    private var imageGlobal: String = ""
    private var nameGlobal: String = ""
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
        initViewModel()
        ObserveProfile()
        event();
    }

    override fun onResume() {
        initLayout()
        observeNetwork()
        super.onResume()
    }


    private fun navigate(id: Int) {
        Navigation.findNavController(binding.root).navigate(id)
    }


    private fun observeNetwork() {
        internetViewModel.getChangeInternet().observe(viewLifecycleOwner, Observer { isConnected ->
            if (!isConnected) {
                binding.layoutSpecial.visibility = View.GONE
                binding.DangNhapProfile.visibility = View.GONE
                binding.imageNoInternetProfile.visibility = View.VISIBLE
            } else {
                if (_shared_Preference.isLogin()) {
                    binding.layoutSpecial.visibility = View.VISIBLE
                    binding.DangNhapProfile.visibility = View.GONE
                } else {
                    binding.layoutSpecial.visibility = View.GONE
                    binding.DangNhapProfile.visibility = View.VISIBLE
                }
                binding.imageNoInternetProfile.visibility = View.GONE
            }
        })
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
            showCustomDialog()
        }
    }

    private fun initViewModel() {
        internetViewModel = ViewModelProvider(this)[InternetViewModel::class.java]
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
                    imageGlobal = it.Image.toString()
                    nameGlobal = it.Name.toString()
                    binding.UserEmailPr.setText(shortString(it.Email.toString()))
                    binding.UserNamPr.setText(shortString(it.Name.toString()))
                }
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == ImagePicker.REQUEST_CODE) {
            val fileUri = data?.data
            if (fileUri != null) {
                lifecycleScope.launch(Dispatchers.IO) {
                    val imageBytes: ByteArray? = getImageBytesFromUri(requireContext(), fileUri)
                    if (imageBytes != null) {
                        ArticleViewModel.postImage(imageBytes)
                    }
                }
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            showToast(requireContext(), "up load fail")
        } else {
            showToast(requireContext(), "Image picker cancel")
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
                    openCamera(requireActivity())
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


    private fun initLayout() {
        if (_shared_Preference.isLogin()) {
            binding.UserNamPr.text = _shared_Preference.getName()
            binding.UserEmailPr.text = _shared_Preference.getEmail()
            when (_shared_Preference.getPermission()) {
                UserEnum.Admin.toString() -> {
                    binding.layoutSpecial.visibility = View.VISIBLE
                    binding.addMin.visibility = View.VISIBLE
                }

                UserEnum.User.toString() -> {
                    binding.layoutSpecial.visibility = View.VISIBLE
                    binding.addMin.visibility = View.GONE
                }

                UserEnum.Author.toString() -> {
                    binding.layoutSpecial.visibility = View.VISIBLE
                    binding.addMin.visibility = View.VISIBLE
                }

                UserEnum.Authorized.toString() -> {
                    binding.layoutSpecial.visibility = View.VISIBLE
                    binding.addMin.visibility = View.VISIBLE
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


    private fun showCustomDialog() {
        var image: ByteArray = ByteArray(10000);
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pop_up_profile)
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

        dialog.findViewById<CircleImageView>(R.id.imageProfileEdit).setOnClickListener {
            ImagePicker.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )    //Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }
        ArticleViewModel.getImage().observe(
            viewLifecycleOwner,
            Observer {
                image = it;
                val bitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
                dialog.findViewById<CircleImageView>(R.id.imageProfileEdit).setImageBitmap(bitmap)
            }
        )

        Glide.with(requireContext())
            .load(imageGlobal)
            .error(R.drawable.none_avatar)
            .fitCenter()
            .into(dialog.findViewById<CircleImageView>(R.id.imageProfileEdit))
        dialog.findViewById<EditText>(R.id.editTextNameProfile).setText(nameGlobal)
        dialog.findViewById<Button>(R.id.btnOkProfile).setOnClickListener {
            if (image.isNotEmpty()) {
                _shared_Preference.getUid()
                    ?.let {
                        ArticleViewModel.setImage(
                            image,
                            it,
                            dialog.findViewById<EditText>(R.id.editTextNameProfile).text.toString()
                        )
                    }
                dialog.dismiss()
            } else if (imageGlobal.isNotEmpty()) {
                image =
                    imageViewToByteArray(dialog.findViewById<CircleImageView>(R.id.imageProfileEdit))
                _shared_Preference.getUid()
                    ?.let {
                        ArticleViewModel.setImage(
                            image,
                            it,
                            dialog.findViewById<EditText>(R.id.editTextNameProfile).text.toString()
                        )
                    }
            }
        }
        dialog.findViewById<Button>(R.id.btnCancelProfile).setOnClickListener {
            dialog.hide()
        }

        dialog.show()
    }
}


fun imageViewToByteArray(imageView: CircleImageView): ByteArray {

    val bitmap: Bitmap = (imageView.drawable).toBitmap()

    // Chuyển đổi Bitmap thành mảng byte
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    return outputStream.toByteArray()
}

fun byteToImage(byteArray: ByteArray): Bitmap {
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
}

@SuppressLint("Recycle")
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

fun openCamera(activity: Activity) {
    ImagePicker.with(activity)
        .crop()                    //Crop image(Optional), Check Customization for more option
        .compress(1024)            //Final image size will be less than 1 MB(Optional)
        .maxResultSize(
            1080,
            1080
        )    //Final image resolution will be less than 1080 x 1080(Optional)
        .start()
}

fun shortString(string: String): String {
    var namePro = "";
    if (string.length > 15) {
        namePro = string.substring(0, 14) + "..."
    } else {
        namePro = string.toString()
    }
    return namePro
}