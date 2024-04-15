package com.example.myappnews.Ui.Fragment.management.Admin.AwaitRequireEdit.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myappnews.Data.Model.Article.NewsArticle
import com.example.myappnews.Interface.Adapter.CommonAdapter
import com.example.myappnews.databinding.ItemWaitEditBinding

class AwaitRequireEditAdapter(private val listArticle: List<NewsArticle>, context: Context) :
    RecyclerView.Adapter<AwaitRequireEditAdapter.ArticleViewHolder>() {

    private lateinit var _onClickListener: CommonAdapter
    private val _context = context
    private var _listArticle = listArticle

    fun setClickListener(OnClickListener: CommonAdapter) {
        this._onClickListener = OnClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(listArticle: List<NewsArticle>) {
        this._listArticle = listArticle
        notifyDataSetChanged()
    }

    inner class ArticleViewHolder(
        private var binding: ItemWaitEditBinding,
        onClick: CommonAdapter
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.apply {
                this.setOnClickListener {
                    onClick.setOnClickListener(adapterPosition)
                }
            }
        }

        fun bind(article: NewsArticle) {
            var status = ""
             if(article.requireEdit==1){
                 status="Đang chờ phê duyệt"
             }else if(article.requireEdit==0){
                 status="Đang chờ chỉnh sửa"
             }else{
                 status="Từ chối chỉnh sửa"
             }
            binding.description.text = article.titleArticle;
            binding.idTrangThai.text=status
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding =
            ItemWaitEditBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

}