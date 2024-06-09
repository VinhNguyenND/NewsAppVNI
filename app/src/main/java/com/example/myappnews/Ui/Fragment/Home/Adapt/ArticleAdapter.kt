package com.example.myappnews.Ui.Fragment.Home.Adapt

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myappnews.Data.Model.Article.Article
import com.example.myappnews.Data.Model.Article.NewsArticle
import com.example.myappnews.Interface.Adapter.CommonAdapter
import com.example.myappnews.R
import com.example.myappnews.Ui.Fragment.Article.comment.calculateElapsedTime
import com.example.myappnews.databinding.ItemArticleBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ArticleAdapter(private val listArticle: List<NewsArticle>, context: Context) :
    RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>(),
    Filterable {

    private lateinit var _onClickListener: CommonAdapter
    private val _context = context
    private var isRequire = false;
    private var _listArticle = listArticle
    private var _listFilter = _listArticle
    fun setClickListener(OnClickListener: CommonAdapter) {
        this._onClickListener = OnClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setIsRequire(boolean: Boolean) {
        this.isRequire = boolean;
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(listArticle: List<NewsArticle>) {
        this._listArticle = listArticle
        this._listFilter = listArticle
        notifyDataSetChanged()
    }

    fun submit(listArticle: List<NewsArticle>) {
//        this._listArticle = listArticle
        this._listFilter = listArticle
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding, _onClickListener)
    }

    override fun getItemCount(): Int {
        if (this._listArticle.isEmpty()) {
            return 0
        }
        return this._listArticle.size
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(this._listArticle[position])
    }

    inner class ArticleViewHolder(private var binding: ItemArticleBinding, onClick: CommonAdapter) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.apply {
                this.setOnClickListener {
                    onClick.setOnClickListener(adapterPosition)
                }
            }
        }

        fun bind(article: NewsArticle) {
            binding.description.text = article.titleArticle
            Glide.with(_context)
                .load(article.imageUrl?.trim())
                .error(R.drawable.ic_news)
                .fitCenter()
                .into(binding.imgArticle)
            if (article.pubDate.toString() != "" && article.pubDate.toString() != "null") {
                binding.txtTimeCal.text = article.pubDate?.let { calculateElapsedTime(it) };
                binding.txtRawTime.text = article.pubDate?.let { convertDateToString(it) }
            } else if (article.pubDate.toString() == "null" || article.pubDate.toString()
                    .isEmpty()
            ) {
                binding.timeArticleCal.visibility = View.GONE
                binding.whenHaveTime.visibility = View.VISIBLE
            }
            if (isRequire) {
                binding.timeArticleCal.visibility = View.GONE
                binding.timeRequireCal.visibility = View.VISIBLE
                binding.txtTimeCalRequire.text = article.requiredDate?.let { calculateElapsedTime(it) };
                binding.txtRawTimeRequire.text = article.requiredDate?.let { convertDateToString(it) }
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val strSearch = constraint.toString();
                _listArticle = if (strSearch.isEmpty()) {
                    _listFilter
                } else {
                    val listFolder = ArrayList<NewsArticle>()
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
                _listArticle = results!!.values as ArrayList<NewsArticle>
                notifyDataSetChanged()
            }

        }
    }
}

fun convertDateToString(date: Date): String {
    val format = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
    return format.format(date)
}