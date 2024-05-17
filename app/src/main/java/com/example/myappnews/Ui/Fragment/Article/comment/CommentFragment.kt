package com.example.myappnews.Ui.Fragment.Article.comment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myappnews.Data.Enum.CommentFilter
import com.example.myappnews.Data.Firebase.ViewModel.ArticleViewModel.ArViewModel
import com.example.myappnews.Data.Model.Article.NewsArticle
import com.example.myappnews.Data.Model.Comment.Comment
import com.example.myappnews.Data.SharedPreferences.Shared_Preference
import com.example.myappnews.Interface.Adapter.CommonAdapter
import com.example.myappnews.Ui.Fragment.management.Author.Home.sha256
import com.example.myappnews.databinding.CommentMainLayoutBinding
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.Date
import kotlin.random.Random

class CommentFragment : Fragment() {
    private lateinit var binding: CommentMainLayoutBinding;
    private val arViewModel = ArViewModel.getInstance()
    private var listComment = ArrayList<Comment>();
    private lateinit var _shared_Preference: Shared_Preference;
    private var article = NewsArticle();
    private var type = CommentFilter.Latest;
    private lateinit var commentItemAdapter: CommentItemAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CommentMainLayoutBinding.inflate(inflater, container, false);
        return binding.root;
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _shared_Preference = Shared_Preference(requireActivity());
        initContent()
        event()
        observe()
        initRcView(requireContext());
    }

    override fun onResume() {
        super.onResume()
    }

    private fun event() {
        binding.btnSendMessage.setOnClickListener {
            var idUser = sha256(LocalDateTime.now().toString())
            var nameUser = generateRandomAlphabet(10);
            if (_shared_Preference.getUid()?.isNotEmpty() == true) {
                idUser = _shared_Preference.getUid()!!;
                if (_shared_Preference.getName()?.isNotEmpty() == true) {
                    nameUser = _shared_Preference.getName()!!
                }
            }
            if (binding.edtInputComment.text.toString() != "") {
                val message = Comment(
                    idArticle = article.idArticle,
                    idComment = sha256(LocalDateTime.now().toString()),
                    idUser = idUser,
                    comment = binding.edtInputComment.text.toString(),
                    numberComment = 0.0,
                    nameUser = nameUser,
                    avatar = article.imageUrl,
                    time = Date(),
                )
                arViewModel.sendMainMessage(message)
                article.idArticle?.let { it1 -> arViewModel.getMainMessage(type, it1) };
                commentItemAdapter.subType(type);
                binding.rcvCommentMain.visibility = View.GONE
                binding.progressCircularComment.visibility = View.VISIBLE
            }
            hideKeyboard(requireContext(),binding.edtInputComment);
        };
        binding.btnLatest.setOnClickListener {
            type = CommentFilter.Latest
            commentItemAdapter.subType(type);
            article.idArticle?.let { it1 -> arViewModel.getMainMessage(type, it1) };
            binding.rcvCommentMain.visibility = View.GONE
            binding.progressCircularComment.visibility = View.VISIBLE
        }
        binding.btnOlder.setOnClickListener {
            type = CommentFilter.Oldest
            commentItemAdapter.subType(type);
            article.idArticle?.let { it1 -> arViewModel.getMainMessage(type, it1) };
            binding.rcvCommentMain.visibility = View.GONE
            binding.progressCircularComment.visibility = View.VISIBLE
        }
        binding.btnMostLike.setOnClickListener {
            type = CommentFilter.Likes
            commentItemAdapter.subType(type);
            article.idArticle?.let { it1 -> arViewModel.getMainMessage(type, it1) };
            binding.rcvCommentMain.visibility = View.GONE
            binding.progressCircularComment.visibility = View.VISIBLE
        }
        binding.btnbackar.setOnClickListener {
            Navigation.findNavController(binding.root).popBackStack()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun initContent() {
        if (arguments?.getParcelable("Article", NewsArticle::class.java) != null) {
            article = arguments?.getParcelable("Article", NewsArticle::class.java)!!
        }
    }

    private fun observe() {
        article.idArticle?.let {
            arViewModel.getMainMessage(type, it).observe(viewLifecycleOwner, Observer {
                if (it.isNotEmpty()) {
                    binding.progressCircularComment.visibility = View.GONE
                    listComment = it;
                    commentItemAdapter.submitList(listComment)
                    binding.rcvCommentMain.visibility = View.VISIBLE
                }
            })
        }
    }

    override fun onPause() {
        super.onPause()
        arViewModel.removeComemt()
    }
    private fun initRcView(context: Context) {
        commentItemAdapter = CommentItemAdapter(listComment, context,_shared_Preference.getUidComment(),article.idArticle!!)
        binding.rcvCommentMain.let {
            it.adapter = commentItemAdapter
            it.layoutManager = LinearLayoutManager(
                parentFragment?.requireContext(),
                RecyclerView.VERTICAL, false
            )
        }
        commentItemAdapter.setClickListener(object : CommonAdapter {
            override fun setOnClickListener(position: Int) {
                val bundle = Bundle()
                bundle.putParcelable("Article", listComment[position])
                bundle.putInt("ishistory", 1)
//                Navigation.findNavController(binding.root).navigate(R.id.article_Fragment, bundle)
            }
        })
    }
}

fun generateRandomAlphabet(length: Int): String {
    val alphabet = ('a'..'z').toList() // Tạo danh sách ký tự từ 'a' đến 'z'
    val random =
        Random(System.currentTimeMillis()) // Tạo một đối tượng Random với seed từ thời điểm hiện tại

    val result = StringBuilder(length)
    repeat(length) {
        val randomIndex = random.nextInt(0, alphabet.size)
        result.append(alphabet[randomIndex])
    }

    return result.toString()
}

fun hideKeyboard(context: Context, editText: EditText) {
    editText.setText("")
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(editText.windowToken, 0)
}