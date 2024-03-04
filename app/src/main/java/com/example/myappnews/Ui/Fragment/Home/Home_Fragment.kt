package com.example.myappnews.Ui.Fragment.Home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myappnews.Data.FakeData.ArticleStore
import com.example.myappnews.Data.Model.Article.Article
import com.example.myappnews.Interface.Adapter.CommonAdapter
import com.example.myappnews.R
import com.example.myappnews.Ui.Fragment.Home.Adapt.ArticleAdapter
import com.example.myappnews.databinding.HomeScreenBinding

class Home_Fragment:Fragment() {
    private lateinit var binding: HomeScreenBinding;
    private lateinit var _articleAdapter:ArticleAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= HomeScreenBinding.inflate(inflater,container,false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView(ArticleStore.listArticle,parentFragment!!.requireContext())

    }
    private fun initRcView(listArticle: MutableList<Article>, context: Context){
        _articleAdapter= ArticleAdapter(listArticle,context)
        binding.homeRecycle.let {
            it.adapter=_articleAdapter
            it.layoutManager= LinearLayoutManager(parentFragment?.requireContext(),
                RecyclerView.VERTICAL,false)
        }
        _articleAdapter.setClickListener(object :CommonAdapter{
            override fun setOnClickListener(position: Int) {
                Navigation.findNavController(binding.root).navigate(R.id.article_Fragment)
            }
        })
    }

}