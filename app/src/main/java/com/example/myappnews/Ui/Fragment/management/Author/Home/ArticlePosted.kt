package com.example.myappnews.Ui.Fragment.management.Author.Home

import android.app.Dialog
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
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.myappnews.Data.Firebase.ViewModel.AdminViewModel.AdminViewModel
import com.example.myappnews.Data.Model.Article.NewsArticle
import com.example.myappnews.Data.constant.dismissKeyboard
import com.example.myappnews.R
import com.example.myappnews.Ui.Fragment.management.Admin.dismissAllKeyboards
import com.example.myappnews.databinding.ArticlePostedBinding

class ArticlePosted : Fragment() {
    private lateinit var binding: ArticlePostedBinding
    private val _adminViewModel = AdminViewModel.getInstance()
    private lateinit var article: NewsArticle;
    private lateinit var idDoc: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ArticlePostedBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initContent()
        event(view)
    }

    override fun onResume() {
        super.onResume()
        initData()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun initContent() {
        if (arguments?.getParcelable("Article", NewsArticle::class.java) != null) {
            article = arguments?.getParcelable("Article", NewsArticle::class.java)!!
            val text: String = article.content!!.replace("\\\\n", "<br/>" + " ");
            binding.txtPageContent.text = Html.fromHtml(text)
            Glide.with(requireContext())
                .load(article.imageUrl?.trim())
                .error(R.drawable.uploaderror)
                .fitCenter()
                .into(binding.imgArticlePage)
        }
    }

    private fun event(view: View) {
        binding.btnbackar.setOnClickListener {
            view.findNavController().popBackStack();
        }
    }

    private fun showCustomDialog(view: View) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.delete_approve_pop)
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
        dialog.findViewById<Button>(R.id.btnDongy).setOnClickListener {
            dismissKeyboard(requireContext(), view)
            dialog.findViewById<ProgressBar>(R.id.progress_pop_approve).visibility = View.VISIBLE
            _adminViewModel.doDelete(idDoc).observe(viewLifecycleOwner, Observer {
                dialog.hide()
                if (it == 1) {
                    showToast(requireContext(), "Bạn đã  xóa thành công")
                    view.findNavController().popBackStack()
                } else if (it == -1) {
                    showToast(requireContext(), "Bạn đã xóa thất bại")
                }
            })

        }
        dialog.findViewById<Button>(R.id.btnLoaiBo).setOnClickListener {
            dialog.findViewById<ProgressBar>(R.id.progress_pop_approve).visibility = View.VISIBLE
            dialog.hide()
        }
        dialog.show()
    }

    private fun initData() {
        _adminViewModel.getIdDoc(article.idArticle!!.trim()).observe(viewLifecycleOwner, Observer {
            idDoc = it;
        })
    }

}