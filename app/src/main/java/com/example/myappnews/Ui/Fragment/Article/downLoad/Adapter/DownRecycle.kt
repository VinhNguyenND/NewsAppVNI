package com.example.myappnews.Ui.Fragment.Article.downLoad.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myappnews.Data.Local.Article.Down.ArticleDownEntity
import com.example.myappnews.Interface.Adapter.CommonAdapter
import com.example.myappnews.R
import com.example.myappnews.Ui.Fragment.Article.comment.calculateElapsedTime
import com.example.myappnews.Ui.Fragment.Home.Adapt.convertDateToString
import com.example.myappnews.databinding.ItemArticleBinding
import java.util.Locale

class DownRecycle(private val listArticle: List<ArticleDownEntity>, context: Context) :
    RecyclerView.Adapter<DownRecycle.ArticleViewHolder>(), Filterable {


    private lateinit var _onClickListener: CommonAdapter
    private val _context = context
    private var _listArticle = listArticle
    private var _listFilter = _listArticle

    fun setClickListener(OnClickListener: CommonAdapter) {
        this._onClickListener = OnClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(listArticle: List<ArticleDownEntity>) {
        this._listArticle = listArticle
        this._listFilter = _listArticle
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
            binding.timeArticleCal.visibility= View.GONE
            binding.whenHaveTime.visibility=View.VISIBLE
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

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val strSearch = constraint.toString();
                _listArticle = if (strSearch.isEmpty()) {
                    _listFilter
                } else {
                    val listFolder = ArrayList<ArticleDownEntity>()
                    for (item in _listFilter) {
                        if (item.titleArticle?.lowercase(Locale.ROOT)
                                ?.contains(strSearch) == true
                        ) {
                            listFolder.add(item)
                        }
                    }
                    listFolder;
                }
                val filterResults = FilterResults()
                filterResults.values = _listArticle
                return filterResults;
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                _listArticle = results!!.values as ArrayList<ArticleDownEntity>
                notifyDataSetChanged()
            }

        }
    }
}