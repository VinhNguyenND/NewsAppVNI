package com.example.myappnews.Ui.Fragment.Article.Search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myappnews.Data.Api.Internet.InternetViewModel
import com.example.myappnews.Data.Firebase.ViewModel.ArticleViewModel.ArViewModel
import com.example.myappnews.Data.Model.Article.NewsArticle
import com.example.myappnews.Interface.Adapter.CommonAdapter
import com.example.myappnews.R
import com.example.myappnews.Ui.Fragment.Home.Adapt.ArticleAdapter
import com.example.myappnews.databinding.SearchArticleFragmentBinding

class SearchFragment : Fragment() {
    private lateinit var binding: SearchArticleFragmentBinding
    private lateinit var internetViewModel: InternetViewModel
    private val ArticleViewModel = ArViewModel.getInstance();
    private var listArticle = ArrayList<NewsArticle>()
    private lateinit var _articleAdapter: ArticleAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SearchArticleFragmentBinding.inflate(inflater, container, false)
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initRcView(requireContext());
        init(view);
        observeNetwork()
    }

    override fun onResume() {
        super.onResume()
        getAllArticle("All", "All")
    }

    private fun initViewModel() {
        internetViewModel = ViewModelProvider(this)[InternetViewModel::class.java]
    }

    private fun observeNetwork() {
        internetViewModel.getChangeInternet().observe(viewLifecycleOwner, Observer { isConnected ->
            if (!isConnected) {
                binding.rcvSearchArticle.visibility = View.GONE
                binding.imageNoInternetSearchArticle.visibility = View.VISIBLE
            } else {
                binding.rcvSearchArticle.visibility = View.VISIBLE
                binding.imageNoInternetSearchArticle.visibility = View.GONE
            }
        })
    }

    private fun init(view: View) {
        binding.btnbackars.setOnClickListener {
            Navigation.findNavController(view).popBackStack()
        }
        binding.searchArticleSv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                _articleAdapter.filter.filter(query)
                return false;
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) {
                    _articleAdapter.submit(ArrayList<NewsArticle>())
                } else {
                    _articleAdapter.submit(listArticle)
                }
                _articleAdapter.filter.filter(newText)
                return false;
            }
        })
    }


    private fun initRcView(context: Context) {
        _articleAdapter = ArticleAdapter(listArticle, context)
        binding.rcvSearchArticle.let {
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

    private fun getAllArticle(topic: String, source: String) {
        ArticleViewModel.getAllNews().observe(viewLifecycleOwner, Observer {
            listArticle = it;
            _articleAdapter.submit(listArticle)

        })
    }
}