package com.example.myappnews.Ui.Fragment.Home.Adapt

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myappnews.Data.Model.Article.Article
import com.example.myappnews.Data.Model.Article.NewsArticle
import com.example.myappnews.Interface.Adapter.CommonAdapter
import com.example.myappnews.R
import com.example.myappnews.databinding.ItemArticleBinding

class ArticleAdapter(private val listArticle: List<NewsArticle>,context: Context):RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>(){

    private lateinit var _onClickListener:CommonAdapter
    private val _context=context
    private var _listArticle=listArticle
    fun setClickListener(OnClickListener:CommonAdapter){
        this._onClickListener=OnClickListener
    }
    @SuppressLint("NotifyDataSetChanged")
    fun submitList(listArticle: List<NewsArticle>){
        this._listArticle=listArticle
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding =ItemArticleBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ArticleViewHolder(binding,_onClickListener)
    }

    override fun getItemCount(): Int {
       if(this._listArticle.isEmpty()){
           return 0
       }
        return this._listArticle.size
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
       holder.bind(this._listArticle[position])
    }
    inner class ArticleViewHolder(private var binding: ItemArticleBinding, onClick: CommonAdapter):RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.apply {
                this.setOnClickListener {
                    onClick.setOnClickListener(adapterPosition)
                }
            }
        }
        fun bind(article: NewsArticle){
             binding.description.text=article.titleArticle
             Glide.with(_context)
                 .load(article.imageUrl?.trim())
                 .error(R.drawable.ic_news)
                 .fitCenter()
                 .into(binding.imgArticle)
        }
    }
}