package com.example.myappnews.Ui.Fragment.Search.Note

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
import com.example.myappnews.Ui.Fragment.Search.Adapt.ItemNoteAdapter
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
        _ItemAdapter = ItemNoteAdapter(listNote, context,ViewModelProvider(this))
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
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (direction == ItemTouchHelper.LEFT) {
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc chắn muốn xóa folder này?")
                        .setPositiveButton("Đồng ý") { _, _ ->
                            listNote.removeAt(viewHolder.adapterPosition)
                            _ItemAdapter.submitList(listNote)
                        }
                        .setNegativeButton("Hủy") { dialog, _ ->
                            _ItemAdapter.notifyItemChanged(viewHolder.adapterPosition)
                            dialog.dismiss()
                        }
                        .setCancelable(false)
                        .show()
                }
            }

            override fun getSwipeDirs(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {

                return ItemTouchHelper.LEFT
            }


            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                RecyclerViewSwipeDecorator.Builder(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                    .addBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.Red
                        )
                    )
                    .addActionIcon(R.drawable.icdelete24)
                    .create()
                    .decorate()

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }

        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.noteFolderRcv)
    }


}