package com.example.myappnews.Ui.Fragment.management.Admin

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.myappnews.Data.Firebase.ViewModel.AdminViewModel.AdminViewModel
import com.example.myappnews.Data.Model.Article.NewsArticle
import com.example.myappnews.Data.SharedPreferences.Shared_Preference
import com.example.myappnews.R
import com.example.myappnews.databinding.EditFragmentBinding

class EditFragment : Fragment() {
    private lateinit var binding: EditFragmentBinding
    private val _adminViewModel = AdminViewModel.getInstance()
    private lateinit var _shared_Preference: Shared_Preference;
    private lateinit var article: NewsArticle;
    private lateinit var idDoc: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = EditFragmentBinding.inflate(inflater, container, false);
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _shared_Preference = Shared_Preference(requireActivity());
        initContent()
        event()
    }

    override fun onResume() {
        initData()
        super.onResume()
    }

    private fun event() {
        binding.btnbackar.setOnClickListener {
            Navigation.findNavController(binding.root).popBackStack()
        }
        binding.btnApprove.setOnClickListener {
            showCustomDialog()
        }
        binding.btnDenied.setOnClickListener {
            showCauseDenied()
        }
    }


    private fun showCustomDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.approve_popup)
        val window: Window? = dialog.window;
        if (window == null) {
            return
        }
        dialog.findViewById<Button>(R.id.btnLoaiBo).text = "Loại Bỏ"
        window.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        val windowAtribute: WindowManager.LayoutParams = window.attributes
        windowAtribute.gravity = Gravity.CENTER
        window.attributes = windowAtribute
        dialog.findViewById<Button>(R.id.btnDongy).setOnClickListener {
            dialog.findViewById<ProgressBar>(R.id.progress_pop_approve).visibility = View.VISIBLE
            _adminViewModel.doApprove(_shared_Preference.getUid().toString(), idDoc, 1)
                .observe(viewLifecycleOwner, Observer {
                    dialog.hide()
                    if (it == true && isVisible && isAdded) {
                        showToast(requireContext(), " chấp nhận thành công")
                    } else {
                        showToast(requireContext(), " chấp nhận thất bại")
                    }
                })
        }
        dialog.findViewById<Button>(R.id.btnLoaiBo).setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showCauseDenied() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.cause_pop_up)
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
        val editText = dialog.findViewById<EditText>(R.id.txtCause)
        dialog.findViewById<Button>(R.id.btnOkCausepop)
            .setOnClickListener {
                val causeDenied = editText.text.toString()
//                if (causeDenied.isEmpty()) {
//                    editText.setError("Please enter information!");
//                    editText.requestFocus();
//                } else {
//                    editText.setError(null);
//
//                }
                val Article = NewsArticle(
                    content = article.content,
                    idArticle = article.idArticle,
                    isApprove = -1,
                    country = article.country,
                    creator = article.creator,
                    field = article.field,
                    hide = true,
                    idPoster = article.idPoster,
                    idReviewer = article.idReviewer,
                    linkArticle = article.linkArticle,
                    imageUrl = article.imageUrl,
                    pubDate = article.pubDate,
                    requireEdit = -1,
                    requiredDate = article.requiredDate,
                    sourceId = article.sourceId,
                    titleArticle = article.titleArticle,
                    sourceUrl = article.sourceUrl,
                    cause = article.cause,
                    causeDenied = causeDenied
                )
                _adminViewModel.approvePush(Article).observe(
                    viewLifecycleOwner,
                    Observer {
                        if (isAdded && isVisible) {
                            if (it == 0) {
                                showToast(requireContext(), "thanh cong");
                            } else {
                                showToast(requireContext(), "that bai");
                                dialog.dismiss()
                            }
                        }
                    }
                );
                dialog.dismiss()
            }

        dialog.findViewById<Button>(R.id.btnCancelpop)
            .setOnClickListener {
                dialog.dismiss();
            }
        dialog.show()
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun initContent() {
        if (arguments?.getParcelable("Article", NewsArticle::class.java) != null) {
            article = arguments?.getParcelable("Article", NewsArticle::class.java)!!
            val text: String = article.content!!.replace("\\\\n", "<br/>" + " ");
            binding.txtPageContent.text = Html.fromHtml(text)
            binding.txtPageTime.text=article.requiredDate.toString()
            Log.d("id", article.idArticle!!)
            binding.articlePageTittle.text = article.titleArticle
            Glide.with(requireContext())
                .load(article.imageUrl?.trim())
                .error(R.drawable.uploaderror)
                .fitCenter()
                .into(binding.imgArticlePage)
        }
    }

    private fun initData() {
        _adminViewModel.getIdDoc(article.idArticle!!.trim()).observe(viewLifecycleOwner, Observer {
            idDoc = it;
            Log.i("doc lay ve:>>", it.toString())
        })

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