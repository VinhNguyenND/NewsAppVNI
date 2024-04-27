package com.example.myappnews.Ui.Fragment.Article.downLoad.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myappnews.Data.Local.Article.Down.ArticleDownEntity
import com.example.myappnews.Interface.Adapter.CommonAdapter
import com.example.myappnews.R
import com.example.myappnews.databinding.ItemArticleBinding

class DownRecycle(private val listArticle: List<ArticleDownEntity>, context: Context) :
    RecyclerView.Adapter<DownRecycle.ArticleViewHolder>() {


    private lateinit var _onClickListener: CommonAdapter
    private val _context = context
    private var _listArticle = listArticle

    fun setClickListener(OnClickListener: CommonAdapter) {
        this._onClickListener = OnClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(listArticle: List<ArticleDownEntity>) {
        this._listArticle = listArticle
        notifyDataSetChanged()
    }

    inner class ArticleViewHolder(
        private var binding: ItemArticleBinding,
        onClick: CommonAdapter
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.apply {
                this.setOnClickListener {
                    onClick.setOnClickListener(adapterPosition)
                }
            }
        }

        fun bind(article: ArticleDownEntity) {
            binding.description.text = article.titleArticle
            Glide.with(_context)
                .load(article.imageUrl?.trim())
                .error(R.drawable.uploaderror)
                .fitCenter()
                .into(binding.imgArticle)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding =
            ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding, _onClickListener)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(_listArticle[position])
    }

    override fun getItemCount(): Int {
        if (this._listArticle.isEmpty()) {
            return 0
        }
        return this._listArticle.size
    }
}