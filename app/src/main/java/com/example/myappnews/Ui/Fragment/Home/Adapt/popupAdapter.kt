package com.example.myappnews.Ui.Fragment.Home.Adapt

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myappnews.Data.Model.Article.Field
import com.example.myappnews.Interface.Adapter.CommonAdapter
import com.example.myappnews.databinding.ItemPopUpHomeBinding

class popupAdapter(private val listArticle: ArrayList<Field>, context: Context) :
    RecyclerView.Adapter<popupAdapter.PopupHomeViewHolder>() {

    private lateinit var _onClickListener: CommonAdapter
    private val _context = context
    private var _listChosse = listArticle

    fun setClickListener(OnClickListener: CommonAdapter) {
        this._onClickListener = OnClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(listArticle: ArrayList<Field>) {
        this._listChosse = listArticle
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopupHomeViewHolder {
        val binding =
            ItemPopUpHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PopupHomeViewHolder(binding, _onClickListener)
    }

    override fun onBindViewHolder(holder: PopupHomeViewHolder, position: Int) {
        holder.bind(this._listChosse[position])
    }

    override fun getItemCount(): Int {
        if (this._listChosse.isEmpty()) {
            return 0
        }
        return this._listChosse.size
    }


    inner class PopupHomeViewHolder(
        private var binding: ItemPopUpHomeBinding,
        onClick: CommonAdapter
    ) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.apply {
                this.setOnClickListener {
                    onClick.setOnClickListener(adapterPosition)
                }
            }
            binding.apply {
                checkbtn.setOnClickListener {
                    onClick.setOnClickListener(adapterPosition)
                }
            }
        }

        fun bind(choose: Field) {
            binding.txtChoose.text = choose.fieldId.toString();
            binding.checkbtn.isChecked = choose.choose;
        }
    }


}
