package com.example.myappnews.Ui.Fragment.Search.Adapt

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myappnews.Data.Local.Dictionary.Entity.DictionaryFolder
import com.example.myappnews.Interface.Adapter.CommonAdapter
import com.example.myappnews.databinding.ItemFolderBinding

class FolderAdapter(private val listItemFolder: List<DictionaryFolder>, context: Context):
    RecyclerView.Adapter<FolderAdapter.ItemViewHolder>(){

    private lateinit var _onClickListener: CommonAdapter
    private val _context=context
    private var _listItemFolder=listItemFolder
    fun setClickListener(OnClickListener:CommonAdapter){
        this._onClickListener=OnClickListener
    }
    @SuppressLint("NotifyDataSetChanged")
    fun submitList(listItemFolder: List<DictionaryFolder>){
        this._listItemFolder=listItemFolder
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =ItemFolderBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ItemViewHolder(binding,_onClickListener)
    }

    override fun getItemCount(): Int {
       if(this._listItemFolder.isEmpty()){
           return 0
       }
        return this._listItemFolder.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
       holder.bind(this._listItemFolder[position])
    }
    inner class ItemViewHolder(private var binding: ItemFolderBinding, onClick: CommonAdapter):RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.apply {
                this.setOnClickListener {
                    onClick.setOnClickListener(adapterPosition)
                }
            }
        }
        @SuppressLint("SetTextI18n")
        fun bind(folder: DictionaryFolder){
            binding.NameFolder.text=folder.nameDictionaryFolder.toString()
            binding.wordAdded.text="Đã thêm "+folder.numDictionary.toString()+ " từ"
        }
    }
}