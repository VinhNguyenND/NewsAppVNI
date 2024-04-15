package com.example.myappnews.Ui.Fragment.Search.Adapt

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.myappnews.Data.Local.Dictionary.Entity.DictionaryFolder
import com.example.myappnews.Data.Local.Dictionary.Helper.DictionaryViewModel
import com.example.myappnews.Interface.Adapter.CommonAdapter
import com.example.myappnews.databinding.ItemFolderBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.collections.ArrayList

class FolderAdapter(
    private val listItemFolder: List<DictionaryFolder>,
    context: Context,
    viewModelProvider: ViewModelProvider
) :
    RecyclerView.Adapter<FolderAdapter.ItemViewHolder>(), Filterable {

    private lateinit var _onClickListener: CommonAdapter
    private val _context = context
    private var _listItemFolder = listItemFolder
    private var _listFilter = listItemFolder
    private var DictionaryFolder1 = viewModelProvider.get(DictionaryViewModel::class.java);
    fun setClickListener(OnClickListener: CommonAdapter) {
        this._onClickListener = OnClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(listItemFolder: List<DictionaryFolder>) {
        this._listItemFolder = listItemFolder
        this._listFilter=listItemFolder
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemFolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding, _onClickListener)
    }

    override fun getItemCount(): Int {
        if (this._listItemFolder.isEmpty()) {
            return 0
        }
        return this._listItemFolder.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(this._listItemFolder[position])
    }

    inner class ItemViewHolder(private var binding: ItemFolderBinding, onClick: CommonAdapter) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.apply {
                this.setOnClickListener {
                    onClick.setOnClickListener(adapterPosition)
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(folder: DictionaryFolder) {
            DictionaryFolder1.getItemNoteByIdFolder(folder.idDictionaryFolder)
                .observeForever {
                    binding.wordAdded.text="Đã thêm "+it.size.toString()+ " từ";
                }
            binding.NameFolder.text = folder.nameDictionaryFolder.toString()
            binding.wordAdded.text = "Đã thêm " + folder.numDictionary.toString() + " từ"
            binding.timeUpdate.text = convertLongToTime(folder.timeUpdate)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val strSearch = constraint.toString();
                _listItemFolder = if (strSearch.isEmpty()) {
                    _listFilter
                } else {
                    val listFolder = ArrayList<DictionaryFolder>()
                    for (item in _listFilter) {
                        if (item.nameDictionaryFolder.lowercase(Locale.ROOT).contains(strSearch)) {
                            listFolder.add(item)
                        }
                    }
                    listFolder;
                }
                Log.d("node:",_listItemFolder.toString())
                val filterResults = FilterResults()
                filterResults.values = _listItemFolder
                return filterResults;
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                _listItemFolder = results!!.values as ArrayList<DictionaryFolder>
                notifyDataSetChanged()
            }

        }
    }
}

fun convertLongToTime(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return format.format(date)
}