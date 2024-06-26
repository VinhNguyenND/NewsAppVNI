package com.example.myappnews.Ui.Fragment.management.Author.Denied

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
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.myappnews.Data.Firebase.ViewModel.AdminViewModel.AdminViewModel
import com.example.myappnews.Data.Firebase.ViewModel.AuthorViewModel.AuthorViewModel
import com.example.myappnews.Data.Model.Article.NewsArticle
import com.example.myappnews.Data.SharedPreferences.Shared_Preference
import com.example.myappnews.Data.constant.dismissKeyboard
import com.example.myappnews.R
import com.example.myappnews.Ui.Fragment.management.Author.Home.showToast
import com.example.myappnews.databinding.EditDeniedBinding
import kotlinx.coroutines.delay

class EditDenied : Fragment() {
    private lateinit var binding: EditDeniedBinding
    private lateinit var article: NewsArticle;
    private val _adminViewModel = AdminViewModel.getInstance()
    private val _authorViewModel = AuthorViewModel.getInstance()
    private lateinit var idDoc: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = EditDeniedBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initContent()
        event(view)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun initContent() {
        if (arguments?.getParcelable("Article", NewsArticle::class.java) != null) {
            article = arguments?.getParcelable("Article", NewsArticle::class.java)!!
            val text: String = article.content!!.replace("\\\\n", "<br/>" + " ");
            binding.causeEdit.text = article.causeDenied
            binding.articlePageTittle.text = article.titleArticle
            binding.txtPageContent.text = Html.fromHtml(text)
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
        binding.btnEditDenied.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("Article", article)
            view.findNavController().navigate(R.id.deniedEdit, bundle)
        }
        binding.btnDeleteDenied.setOnClickListener {
            showCustomDialog(view)
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
        dialog.findViewById<TextView>(R.id.textView).text = "bạn có chắc muốn xóa yêu cầu";
        dialog.findViewById<Button>(R.id.btnDongy).setOnClickListener {
            dismissKeyboard(requireActivity(),view)
            dialog.findViewById<ProgressBar>(R.id.progress_pop_approve).visibility = View.VISIBLE
            _authorViewModel.deleteDenied(newsArticle = article)
                .observe(viewLifecycleOwner, Observer {
                    dialog.hide()
                    if (it == true && isAdded) {
                        showToast(requireContext(), "Bạn đã  xóa thành công")
                        view.findNavController().popBackStack()
                    } else if (it == false) {
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

}