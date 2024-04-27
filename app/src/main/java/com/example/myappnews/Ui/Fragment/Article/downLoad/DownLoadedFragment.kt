package com.example.myappnews.Ui.Fragment.Article.downLoad

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myappnews.Data.Local.Article.Down.ArticleDownEntity
import com.example.myappnews.Data.Local.Article.Down.ArticleDownViewModel
import com.example.myappnews.Interface.Adapter.CommonAdapter
import com.example.myappnews.R
import com.example.myappnews.Ui.Fragment.Article.downLoad.Adapter.DownRecycle
import com.example.myappnews.databinding.DownloadedfragmentBinding

class DownLoadedFragment : Fragment() {
    private lateinit var articleDownViewModel: ArticleDownViewModel
    private lateinit var binding: DownloadedfragmentBinding
    private var listArticle = ArrayList<ArticleDownEntity>()
    private lateinit var _articleAdapter: DownRecycle
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DownloadedfragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViewModel()
        initRcView(requireContext());
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initRcView(context: Context) {
        _articleAdapter = DownRecycle(listArticle, context)
        binding.rcvArticleDown.let {
            it.adapter = _articleAdapter
            it.layoutManager = LinearLayoutManager(
                parentFragment?.requireContext(),
                RecyclerView.VERTICAL, false
            )
        }
        _articleAdapter.setClickListener(object : CommonAdapter {
            override fun setOnClickListener(position: Int) {
                val bundle = Bundle()
                bundle.putParcelable("Article", listArticle[position])
                Navigation.findNavController(binding.root).navigate(R.id.articleDown, bundle)
            }
        })
    }


    override fun onResume() {
        super.onResume()
        articleDownViewModel.readAllArticle.observe(
            viewLifecycleOwner, Observer {
                val Article = ArrayList<ArticleDownEntity>()
                for (doc in it) {
                    Article.add(doc)
                }
                listArticle = Article
                _articleAdapter.submitList(listArticle)
                Log.d("dữ liệu đã tải", listArticle[0].idArticle)
            }
        )
    }

    private fun initViewModel() {
        articleDownViewModel = ViewModelProvider(this).get(ArticleDownViewModel::class.java)
    }

}