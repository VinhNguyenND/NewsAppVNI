package com.example.myappnews.Ui.Fragment.Search.Adapt

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.myappnews.Data.Local.Dictionary.Entity.DictionaryItem
import com.example.myappnews.Data.Local.Dictionary.Helper.DictionaryViewModel
import com.example.myappnews.Interface.Adapter.CommonAdapter
import com.example.myappnews.databinding.ItemNotesBinding

class ItemNoteAdapter(private val listItemNote: List<DictionaryItem>, context: Context,viewModelProvider: ViewModelProvider) :
    RecyclerView.Adapter<ItemNoteAdapter.ItemViewHolder>() {
    private lateinit var _onClickListener: CommonAdapter
    private val _context = context
    private val _viewModelProvider=viewModelProvider
    private var _listItemNote = listItemNote


    @SuppressLint("NotifyDataSetChanged")
    fun submitList(listItemFolder: List<DictionaryItem>) {
        this._listItemNote = listItemFolder
        notifyDataSetChanged()
    }

    fun setClickListener(OnClickListener: CommonAdapter) {
        this._onClickListener = OnClickListener
    }

    inner class ItemViewHolder(private var binding: ItemNotesBinding, onClick: CommonAdapter) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.apply {
                this.setOnClickListener {
                    onClick.setOnClickListener(adapterPosition)
                }
            }
        }


        fun bind(note: DictionaryItem) {

            binding.wordItem.text = note.word.toString()+"\n"+note.wordMean.toString();
            binding.txtContentItemNote.text = note.mean.toString();
            binding.txtPhoneticItem.text = note.phonetic.toString();
            binding.btnRunVoice.setOnClickListener {
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemNotesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding, _onClickListener)
    }

    override fun getItemCount(): Int {
        if (this._listItemNote.isEmpty()) {
            return 0
        }
        return this._listItemNote.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(_listItemNote[position])
    }


}