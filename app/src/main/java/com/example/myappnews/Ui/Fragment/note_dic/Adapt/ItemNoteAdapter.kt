package com.example.myappnews.Ui.Fragment.note_dic.Adapt

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.myappnews.Data.Local.Dictionary.Entity.DictionaryItem
import com.example.myappnews.Data.Local.Dictionary.Helper.DictionaryViewModel
import com.example.myappnews.Interface.Adapter.CommonAdapter
import com.example.myappnews.R
import com.example.myappnews.databinding.ItemNotesBinding

class ItemNoteAdapter(
    private val listItemNote: List<DictionaryItem>,
    context: Context,
    viewModelProvider: ViewModelProvider,
    private val DictionaryFolder: DictionaryViewModel
) :
    RecyclerView.Adapter<ItemNoteAdapter.ItemViewHolder>() {
    private lateinit var _onClickListener: CommonAdapter
    private val _context = context
    private val _viewModelProvider = viewModelProvider
    private var _listItemNote = listItemNote
    private var _dictionaryFolder = DictionaryFolder


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
            binding.wordItem.text = note.word.toString() + "\n" + note.wordMean.toString();
            binding.txtContentItemNote.text = note.mean.toString();
            binding.txtPhoneticItem.text = note.phonetic.toString();
            binding.btnDeleteNote.setOnClickListener {
                showDeleteDialog(_context, note)
            }
        }
    }

    private fun showDeleteDialog(context: Context, note: DictionaryItem) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.delete_approve_pop)
        val window: Window = dialog.window ?: return;
        window.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.findViewById<TextView>(R.id.textView).text = "Bạn có muốn xóa ";
        val windowAtribute: WindowManager.LayoutParams = window.attributes
        windowAtribute.gravity = Gravity.CENTER
        window.attributes = windowAtribute

        dialog.findViewById<Button>(R.id.btnDongy).setOnClickListener {
            _dictionaryFolder.deleteDictionaryItemById(note.idDictionaryItem)
            dialog.dismiss()
        }
        dialog.findViewById<Button>(R.id.btnLoaiBo).setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
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