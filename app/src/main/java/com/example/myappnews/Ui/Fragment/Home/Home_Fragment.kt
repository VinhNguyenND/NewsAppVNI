package com.example.myappnews.Ui.Fragment.Home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myappnews.Data.FakeData.ArticleStore
import com.example.myappnews.Data.Firebase.ViewModel.ArticleViewModel.ArViewModel
import com.example.myappnews.Data.Model.Article.Article
import com.example.myappnews.Data.Model.Article.NewsArticle
import com.example.myappnews.Interface.Adapter.CommonAdapter
import com.example.myappnews.R
import com.example.myappnews.Ui.Fragment.Home.Adapt.ArticleAdapter
import com.example.myappnews.databinding.HomeScreenBinding

class Home_Fragment : Fragment() {
    private lateinit var binding: HomeScreenBinding;
    private lateinit var _articleAdapter: ArticleAdapter
    private val ArticleViewModel = ArViewModel.getInstance();
    private var listArticle = ArrayList<NewsArticle>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeScreenBinding.inflate(inflater, container, false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initShimer()
        initRcView(requireParentFragment().requireContext())
    }

    private fun initRcView(context: Context) {
        _articleAdapter = ArticleAdapter(listArticle, context)
        binding.homeRecycle.let {
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

    override fun onResume() {
        super.onResume()
        getAllArticle()
    }

    override fun onPause() {
        super.onPause()
        listArticle = ArrayList<NewsArticle>()
    }

    private fun getAllArticle() {
        binding.shimmerViewContainer.startShimmer()
        ArticleViewModel.getAllArticle().observe(viewLifecycleOwner, Observer {
            listArticle = it;
            _articleAdapter.submitList(it);
            binding.shimmerViewContainer.stopShimmer()
            binding.shimmerViewContainer.hideShimmer()
            binding.shimmerViewContainer.visibility = View.GONE
            Log.i("du lieu lay ve", it.toString());
        })
    }

    private fun initShimer() {
        binding.shimmerViewContainer.visibility = View.VISIBLE
    }

}