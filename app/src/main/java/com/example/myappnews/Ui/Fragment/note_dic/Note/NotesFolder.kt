package com.example.myappnews.Ui.Fragment.note_dic.Note

import android.app.AlertDialog
import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myappnews.Data.Local.Dictionary.Entity.DictionaryFolder
import com.example.myappnews.Data.Local.Dictionary.Entity.DictionaryItem
import com.example.myappnews.Data.Local.Dictionary.Helper.DictionaryViewModel
import com.example.myappnews.Interface.Adapter.CommonAdapter
import com.example.myappnews.R
import com.example.myappnews.Ui.Fragment.note_dic.Adapt.ItemNoteAdapter
import com.example.myappnews.Ui.Fragment.management.Author.Home.showToast
import com.example.myappnews.databinding.NotesFolderBinding
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

class NotesFolder : Fragment() {
    private lateinit var binding: NotesFolderBinding
    private var dictionary: DictionaryFolder? = null;
    private lateinit var DictionaryFolder1: DictionaryViewModel
    private var listNote = ArrayList<DictionaryItem>()
    private lateinit var _ItemAdapter: ItemNoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = NotesFolderBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        event()
        initContent()
        initViewModel()
        initRcView(requireContext())
    }

    private fun initViewModel() {
        DictionaryFolder1 = ViewModelProvider(this).get(DictionaryViewModel::class.java)
    }

    override  fun onResume() {
        super.onResume()
        dictionary?.let {
            DictionaryFolder1.getItemNoteByIdFolder(it.idDictionaryFolder).observe(
                viewLifecycleOwner,
                Observer {
                    listNote = ArrayList<DictionaryItem>()
                    for (doc in it) {
                        listNote.add(doc)
                    }
                    _ItemAdapter.submitList(listNote)
                }
            )
        }
    }

    private fun event() {
        binding.btnBackNoteFolder.setOnClickListener {
            Navigation.findNavController(binding.root).popBackStack()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun initContent() {
        if (arguments?.getParcelable("Article", DictionaryFolder::class.java) != null) {
            dictionary = arguments?.getParcelable("Article", DictionaryFolder::class.java)!!
            showToast(requireContext(), dictionary!!.idDictionaryFolder.toString())
        }
    }

    private fun initRcView(context: Context) {
        _ItemAdapter = ItemNoteAdapter(listNote, context,ViewModelProvider(this),DictionaryFolder1)
        binding.noteFolderRcv.let {
            it.adapter = _ItemAdapter
            it.layoutManager = LinearLayoutManager(
                parentFragment?.requireContext(),
                RecyclerView.VERTICAL, false
            )
        }
        _ItemAdapter.setClickListener(object : CommonAdapter {
            override fun setOnClickListener(position: Int) {

            }
        })
    }


}