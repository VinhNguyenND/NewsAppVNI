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
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.myappnews.Data.Firebase.ViewModel.AdminViewModel.AdminViewModel
import com.example.myappnews.Data.Model.Article.Article
import com.example.myappnews.Data.Model.Article.NewsArticle
import com.example.myappnews.Data.constant.dismissKeyboard
import com.example.myappnews.R
import com.example.myappnews.Ui.Fragment.management.Admin.dismissAllKeyboards
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
        event(view)
    }

    private fun event(view: View) {
        binding.btnApproveRqEdit.setOnClickListener {
            showYesDialog(view)
        }
        binding.btnXoaRequiredEdit.setOnClickListener {
            showDeleteDialog(view)
        }
        binding.idEditAgain.setOnClickListener {
            showAgainEdit(view)
        }
        binding.btnbackadEdit.setOnClickListener {
            Navigation.findNavController(binding.root).popBackStack()
        }
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun initContent() {
        if (arguments?.getParcelable("Article", NewsArticle::class.java) != null) {
            article = arguments?.getParcelable("Article", NewsArticle::class.java)!!
            val text: String = article.content!!.replace("\\\\n", "<br/>" + " ");
            binding.txtPageContent.text = Html.fromHtml(text)
            binding.articlePageTittle.text = article.titleArticle
            binding.txtPageTime.text = article.pubDate.toString()
            binding.idTrangthaiEidit.text = article.requireEdit?.let { resonseStatus(it) }
            article.pubDate.toString()
            Glide.with(requireContext())
                .load(article.imageUrl?.trim())
                .error(R.drawable.uploaderror)
                .fitCenter()
                .into(binding.imgArticlePage)
        }

    }

    private fun showYesDialog(view: View) {
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
            dismissKeyboard(requireContext(), view)
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
                        dismissAllKeyboards(requireActivity())
                        view.findNavController().popBackStack()
                    } else if (it == 0) {
                        dismissAllKeyboards(requireActivity())
                        showToast(requireContext(), "Cập nhật thất bại")
                    } else {
                        dismissAllKeyboards(requireActivity())
                        showToast(requireContext(), "Lỗi cập nhật")
                    }
                }
            )
            dialog.dismiss()
        }
        cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showDeleteDialog(view: View) {
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
            dismissKeyboard(requireContext(), view)
            _adminViewModel.deleteRequireEdit(article.idArticle.toString())
                .observe(viewLifecycleOwner, Observer {
                    dialog.hide()
                    if (it == 1) {
                        showToast(requireContext(), "Bạn đã  xóa thành công")
                        dismissAllKeyboards(requireActivity())
                        view.findNavController().popBackStack()
                    } else if (it == -1) {
                        dismissAllKeyboards(requireActivity())
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

    private fun showAgainEdit(view: View) {
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
                        if (isAdded && isVisible) {
                            if (it == 0) {
                                view.findNavController().popBackStack()
                                dismissAllKeyboards(requireActivity())
                                showToast(requireContext(), "thanh cong");
                            } else {
                                dismissAllKeyboards(requireActivity())
                                showToast(requireContext(), "that bai");
                                dialog.dismiss()
                            }
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

fun resonseStatus(status: Int): String {
    var status1 = "";
    if (status == 1) {
        status1 = "Đang chờ phê duyệt"
    } else if (status == 0) {
        status1 = "Đang chờ chỉnh sửa"
    } else {
        status1 = "Từ chối chỉnh sửa"
    }
    return status1;
}