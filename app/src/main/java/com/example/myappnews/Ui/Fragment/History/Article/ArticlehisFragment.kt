package com.example.myappnews.Ui.Fragment.History.Article

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myappnews.Data.Firebase.ViewModel.ArticleViewModel.ArViewModel
import com.example.myappnews.Data.Local.Article.ArticlelocalViewModel
import com.example.myappnews.Data.Model.Article.NewsArticle
import com.example.myappnews.Interface.Adapter.CommonAdapter
import com.example.myappnews.R
import com.example.myappnews.Ui.Fragment.Article.Article_Fragment
import com.example.myappnews.Ui.Fragment.Article.toNewsArticle
import com.example.myappnews.Ui.Fragment.Home.Adapt.ArticleAdapter
import com.example.myappnews.databinding.HistoryArticleBinding

class ArticlehisFragment(context: Context) : Fragment() {
    private lateinit var binding: HistoryArticleBinding
    private val _context = context
    private lateinit var articlelocalViewModel: ArticlelocalViewModel
    private lateinit var _articleAdapter: ArticleAdapter
    private var listArticle = listOf<NewsArticle>()

    override fun onResume() {
        super.onResume()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HistoryArticleBinding.inflate(inflater, container, false);
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initRcView(_context)
        getAllArticle()
    }

    private fun initViewModel() {
        articlelocalViewModel = ViewModelProvider(this).get(ArticlelocalViewModel::class.java)
    }

    private fun initRcView(context: Context) {
        _articleAdapter = ArticleAdapter(listArticle, context)
        binding.rcyArHisHome.let {
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
                Navigation.findNavController(binding.root).navigate(R.id.article_Fragment, bundle)
            }
        })
    }



    private fun getAllArticle() {
        articlelocalViewModel.readAllArticle.observe(viewLifecycleOwner, Observer {
            val article1 = ArrayList<NewsArticle>();
            for (doc in it) {
                article1.add(toNewsArticle(doc))
            }
            listArticle = article1.toList()
            _articleAdapter.submitList(listArticle);
        })
    }
}