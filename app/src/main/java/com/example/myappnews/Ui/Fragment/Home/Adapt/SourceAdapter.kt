package com.example.myappnews.Ui.Fragment.Home.Adapt

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myappnews.Data.Model.Source.Source
import com.example.myappnews.Interface.Adapter.CommonAdapter
import com.example.myappnews.databinding.ItemPopUpHomeBinding

class SourceAdapter (private val listSource: ArrayList<Source>, context: Context) :
    RecyclerView.Adapter<SourceAdapter.PopupSourceViewHolder>() {

    private lateinit var _onClickListener: CommonAdapter
    private val _context = context
    private var _listChosse = listSource

    fun setClickListener(OnClickListener: CommonAdapter) {
        this._onClickListener = OnClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(listSource: ArrayList<Source>) {
        this._listChosse = listSource
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopupSourceViewHolder {
        val binding =
            ItemPopUpHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PopupSourceViewHolder(binding, _onClickListener)
    }

    override fun onBindViewHolder(holder: PopupSourceViewHolder, position: Int) {
        holder.bind(this._listChosse[position])
    }

    override fun getItemCount(): Int {
        if (this._listChosse.isEmpty()) {
            return 0
        }
        return this._listChosse.size
    }


    inner class PopupSourceViewHolder(
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

        fun bind(choose: Source) {
            binding.txtChoose.text = choose.SourceName.toString();
            binding.checkbtn.isChecked = choose.choose;
        }
    }


}