package com.example.myappnews.Ui.Fragment.management.Admin

import android.app.Dialog
import android.content.Context
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
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.myappnews.Data.Firebase.ViewModel.AdminViewModel.AdminViewModel
import com.example.myappnews.Data.Model.Article.Article
import com.example.myappnews.Data.Model.Article.NewsArticle
import com.example.myappnews.R
import com.example.myappnews.databinding.EditApprovedBinding
import java.util.Date

class EditApprovedFrg : Fragment() {
    private lateinit var binding: EditApprovedBinding
    private val _adminViewModel = AdminViewModel.getInstance()
    private lateinit var article: NewsArticle;
    private lateinit var idDoc: String
    private var isHide = false;
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = EditApprovedBinding.inflate(inflater, container, false);
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initContent()
        event()
    }

    override fun onResume() {
        super.onResume()
        initData()
    }

    override fun onPause() {
        super.onPause()
        _adminViewModel.set_IsDelete()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun initContent() {
        if (arguments?.getParcelable("Article", NewsArticle::class.java) != null) {
            article = arguments?.getParcelable("Article", NewsArticle::class.java)!!
            val text: String = article.content!!.replace("\\\\n", "<br/>" + " ");
            binding.txtPageContent.text = Html.fromHtml(text)
            binding.articlePageTittle.text = article.titleArticle
            if (article.hide == true) {
                binding.editApproveShow.setImageResource(R.drawable.ichide24)
                isHide = true
            }
            Glide.with(requireContext())
                .load(article.imageUrl?.trim())
                .error(R.drawable.uploaderror)
                .fitCenter()
                .into(binding.imgArticlePage)
        }
    }

    private fun event() {
        binding.btnbackar.setOnClickListener {
            Navigation.findNavController(binding.root).popBackStack()
        }
        binding.deleteBtnApprove.setOnClickListener {
            showCustomDialog()
        }
        binding.editApproveShow.setOnClickListener {
            if (isHide) {
                isHide = false
                _adminViewModel.doHide(idDoc, false)
                binding.editApproveShow.setImageResource(R.drawable.ichide24)
            } else {
                isHide = true
                _adminViewModel.doHide(idDoc, true)
                binding.editApproveShow.setImageResource(R.drawable.icshow24)
            }
        }
        binding.editApprove.setOnClickListener {
            showRequestEdit();
        }
    }

    private fun showCustomDialog() {
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
            dialog.findViewById<ProgressBar>(R.id.progress_pop_approve).visibility = View.VISIBLE
            _adminViewModel.doDelete(idDoc).observe(viewLifecycleOwner, Observer {
                dialog.hide()
                if (it == 1) {
                    showToast(requireContext(), "Bạn đã  xóa thành công")
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

    private fun showRequestEdit() {
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
        dialog.findViewById<Button>(R.id.btnOkCausepop)
            .setOnClickListener {
                val cause = dialog.findViewById<EditText>(R.id.txtCause).text.toString()
                val Article = Article(
                    content = article.content,
                    idArticle = article.idArticle,
                    isApprove = article.isApprove,
                    country = article.country,
                    creator = article.creator,
                    field = article.field,
                    hide = article.hide,
                    idPoster = article.idPoster,
                    idReviewer = article.idReviewer,
                    linkArticle = article.linkArticle,
                    imageUrl = article.imageUrl,
                    pubDate = article.pubDate,
                    requireEdit = 0,
                    requiredDate = Date(),
                    sourceId = article.sourceId,
                    titleArticle = article.titleArticle,
                    sourceUrl = article.sourceUrl,
                    cause = cause
                )
                dialog.dismiss()
                _adminViewModel.sendRequestEdit(Article).observe(
                    viewLifecycleOwner,
                    Observer {
                        if (it == 0) {
                            showToast(requireContext(), "thanh cong");
                        } else {
                            showToast(requireContext(), "that bai");
                            dialog.dismiss()
                        }
                    }
                );
            }
        dialog.findViewById<Button>(R.id.btnCancelpop)
            .setOnClickListener {
                dialog.dismiss();
            }
        dialog.show()
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

    private fun initData() {
        _adminViewModel.getIdDoc(article.idArticle!!.trim()).observe(viewLifecycleOwner, Observer {
            idDoc = it;
        })
    }
}