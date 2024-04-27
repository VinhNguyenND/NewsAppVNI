package com.example.myappnews.Ui.Fragment.management.Admin.AwaitRequireEdit

import android.app.Dialog
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
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.myappnews.Data.Firebase.ViewModel.AdminViewModel.AdminViewModel
import com.example.myappnews.Data.Model.Article.Article
import com.example.myappnews.Data.Model.Article.NewsArticle
import com.example.myappnews.R
import com.example.myappnews.Ui.Fragment.management.Author.Home.showToast
import com.example.myappnews.databinding.AwaitRqEditBinding
import java.util.Date

class AwaitRqEdit : Fragment() {
    private lateinit var binding: AwaitRqEditBinding
    private lateinit var article: NewsArticle;
    private val _adminViewModel = AdminViewModel.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AwaitRqEditBinding.inflate(inflater, container, false);
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initContent()
        event()
    }

    private fun event() {
        binding.btnApproveRqEdit.setOnClickListener {
            showYesDialog()
        }
        binding.btnXoaRequiredEdit.setOnClickListener {
            showDeleteDialog()
        }
        binding.idEditAgain.setOnClickListener {
            showAgainEdit()
        }
    }

    private fun observe() {

    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun initContent() {
        if (arguments?.getParcelable("Article", NewsArticle::class.java) != null) {
            article = arguments?.getParcelable("Article", NewsArticle::class.java)!!
            val text: String = article.content!!.replace("\\\\n", "<br/>" + " ");
            binding.txtPageContent.text = Html.fromHtml(text)
            binding.articlePageTittle.text = article.titleArticle


            Glide.with(requireContext())
                .load(article.imageUrl?.trim())
                .error(R.drawable.uploaderror)
                .fitCenter()
                .into(binding.imgArticlePage)
        }

    }

    private fun showYesDialog() {
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
        text.text = "Bạn có đồng ý với bài chỉnh sửa"
        ok.setOnClickListener {
            val Article = NewsArticle(
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
                requireEdit = article.requireEdit,
                requiredDate = article.requiredDate,
                sourceId = article.sourceId,
                titleArticle = article.titleArticle,
                sourceUrl = article.sourceUrl,
                cause = article.cause,
            );
            _adminViewModel.publishRequired(Article).observe(
                viewLifecycleOwner,
                Observer {
                    if (it == 1) {
                        showToast(requireContext(), "Cập nhật thành công")
                    } else if (it == 0) {
                        showToast(requireContext(), "Cập nhật thất bại")
                    } else {
                        showToast(requireContext(), "Lỗi cập nhật")
                    }
                }
            )
            Log.d("ban da lay du lieu de chap nhan",Article.toString())
            dialog.dismiss()
        }
        cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showDeleteDialog() {
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
        text.text = "Bạn có chắc muốn xóa yêu cầu "
        ok.setOnClickListener {
            _adminViewModel.deleteRequireEdit(article.idArticle.toString())
                .observe(viewLifecycleOwner, Observer {
                    dialog.hide()
                    if (it == 1) {
                        showToast(requireContext(), "Bạn đã  xóa thành công")
                    } else if (it == -1) {
                        showToast(requireContext(), "Bạn đã xóa thất bại")
                    }
                })
            dialog.dismiss()
        }
        cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showAgainEdit() {
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
        val cause = dialog.findViewById<EditText>(R.id.txtCause)
        dialog.findViewById<TextView>(R.id.textView2).text =
            "Bạn có muốn gửi lại yêu cầu chỉnh sửa";
        cause.hint = "Lý do chỉnh sủa lại";
        dialog.findViewById<Button>(R.id.btnOkCausepop)
            .setOnClickListener {
                val Article = NewsArticle(
                    content = article.content,
                    idArticle = article.idArticle,
                    isApprove = 1,
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
                    requiredDate = article.requiredDate,
                    sourceId = article.sourceId,
                    titleArticle = article.titleArticle,
                    sourceUrl = article.sourceUrl,
                    cause = cause.text.toString()
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
}