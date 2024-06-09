package com.example.myappnews.Ui.Fragment.Article.comment

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myappnews.Data.Enum.CommentFilter
import com.example.myappnews.Data.Firebase.ViewModel.ArticleViewModel.ArViewModel
import com.example.myappnews.Data.Model.Comment.Comment
import com.example.myappnews.Interface.Adapter.CommonAdapter
import com.example.myappnews.R
import com.example.myappnews.Ui.Fragment.management.Author.Home.showToast
import com.example.myappnews.databinding.ItemCommentsBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class CommentItemAdapter(
    private val listComment: List<Comment>,
    context: Context,
    id: String,
    idArticle: String
) :
    RecyclerView.Adapter<CommentItemAdapter.CommentViewHolder>() {
    private val arViewModel = ArViewModel.getInstance()
    private lateinit var _onClickListener: CommonAdapter
    private val _context = context
    private var subType: CommentFilter = CommentFilter.Latest
    private var _listItemComment = listComment
    private var _id = id
    private val _idArticle = idArticle;

    fun setClickListener(OnClickListener: CommonAdapter) {
        this._onClickListener = OnClickListener
    }

    fun subType(commentFilter: CommentFilter) {
        this.subType = commentFilter
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(listComment: List<Comment>) {
        this._listItemComment = listComment
        notifyDataSetChanged()
    }

    inner class CommentViewHolder(
        private var binding: ItemCommentsBinding,
        onClick: CommonAdapter
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.apply {
                this.setOnClickListener {
                    onClick.setOnClickListener(adapterPosition)
                }
            }
        }

        fun bind(comment: Comment) {
            binding.txtNameComment.text = comment.nameUser;
            binding.txtContentComment.text = comment.comment;
            binding.txtTimeComment.text = comment.time?.let { calculateElapsedTime(it).trim() }
            if (comment.like.isNotEmpty() && comment.like.contains(_id)) {
                binding.btnLikeComment.setImageResource(R.drawable.iclikeheart)
            }
            Glide.with(_context)
                .load(comment.avatar?.trim())
                .error(R.drawable.none_avatar)
                .fitCenter()
                .into(binding.imageAvatarComment)
            var size1 = 0;
            if (comment.like.isNotEmpty()) {
                size1 = comment.like.size
            }
            binding.txtNumberLike.text = size1.toString()
            binding.btnDelete.setOnClickListener {
                if (_listItemComment.size == 1) {
                    submitList(ArrayList<Comment>())
                }
                arViewModel.deleteComment(comment).observeForever {
                    if (it) {
                        arViewModel.getMainMessage(subType, _idArticle);
                    }
                }
            }
            binding.btnLikeComment.setOnClickListener {
                if (comment.like.contains(_id)) {
                    val index = comment.like.indexOf(_id);
                    comment.like.removeAt(index);
                    showToast(_context, comment.like.size.toString())
                    binding.btnLikeComment.setImageResource(R.drawable.iclike)
                    arViewModel.likeComment(comment, _id, false)
                    size1 = comment.like.size
                    binding.txtNumberLike.text = size1.toString()
                } else {
                    var size = comment.like.size
                    size += 1
                    binding.txtNumberLike.text = size.toString()
                    binding.btnLikeComment.setImageResource(R.drawable.iclikeheart)
                    arViewModel.likeComment(comment, _id, true)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding =
            ItemCommentsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(binding, _onClickListener)
    }

    override fun getItemCount(): Int {
        if (this._listItemComment.isEmpty()) {
            return 0
        }
        return this._listItemComment.size
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(this._listItemComment[position])
    }
}


fun calculateElapsedTime(commentDateTime: Date): String {
    val currentDateTime = Date()
    val elapsedTimeMillis = currentDateTime.time - commentDateTime.time

    val seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTimeMillis)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTimeMillis)
    val hours = TimeUnit.MILLISECONDS.toHours(elapsedTimeMillis)
    val days = TimeUnit.MILLISECONDS.toDays(elapsedTimeMillis)

    return when {
        days > 0 -> "$days day${if (days > 1) "s" else ""} ago"
        hours > 0 -> "$hours hour${if (hours > 1) "s" else ""} ago"
        minutes > 0 -> "$minutes minute${if (minutes > 1) "s" else ""} ago"
        else -> "$seconds second${if (seconds > 1) "s" else ""} ago"
    }
}
