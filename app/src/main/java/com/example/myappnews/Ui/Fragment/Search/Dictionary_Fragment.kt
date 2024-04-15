package com.example.myappnews.Ui.Fragment.Search

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Canvas
import android.os.Bundle
import android.text.Layout.Directions
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myappnews.Data.Local.Dictionary.Entity.DictionaryFolder
import com.example.myappnews.Data.Local.Dictionary.Helper.DictionaryViewModel
import com.example.myappnews.Interface.Adapter.CommonAdapter
import com.example.myappnews.R
import com.example.myappnews.Ui.Fragment.Search.Adapt.FolderAdapter
import com.example.myappnews.Ui.Fragment.management.Author.Home.showToast
import com.example.myappnews.databinding.SearchScreenBinding
import com.google.android.material.button.MaterialButton
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Dictionary_Fragment : Fragment() {
    private lateinit var binding: SearchScreenBinding
    private lateinit var DictionaryFolder: DictionaryViewModel
    private lateinit var _folderAdapter: FolderAdapter
    private var listFolder = ArrayList<DictionaryFolder>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SearchScreenBinding.inflate(inflater, container, false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initRcView(requireContext())
        event()
    }

    override fun onResume() {
        super.onResume()
        DictionaryFolder.readAllDicFolder.observe(
            viewLifecycleOwner,
            Observer {
                listFolder = ArrayList<DictionaryFolder>()
                for (doc in it) {
                    listFolder.add(doc)
                }
                _folderAdapter.submitList(listFolder)
                Log.d("folder lay ve tu local", listFolder.toString())
            }
        )
    }

    private fun initViewModel() {
        DictionaryFolder = ViewModelProvider(this).get(DictionaryViewModel::class.java)
    }

    private fun initRcView(context: Context) {
        _folderAdapter = FolderAdapter(listFolder, context,ViewModelProvider(this))
        binding.rcvDictionary.let {
            it.adapter = _folderAdapter
            it.layoutManager = LinearLayoutManager(
                parentFragment?.requireContext(),
                RecyclerView.VERTICAL, false
            )
        }
        _folderAdapter.setClickListener(object : CommonAdapter {
            override fun setOnClickListener(position: Int) {
                val bundle = Bundle()
                bundle.putParcelable("Article", listFolder[position])
                Navigation.findNavController(binding.root).navigate(R.id.notesFolder, bundle)
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
                            listFolder.removeAt(viewHolder.adapterPosition)
                            _folderAdapter.submitList(listFolder)
                        }
                        .setNegativeButton("Hủy") { dialog, _ ->
                            _folderAdapter.notifyItemChanged(viewHolder.adapterPosition)
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
        itemTouchHelper.attachToRecyclerView(binding.rcvDictionary)
    }

    private fun event() {
        binding.idAddDic.setOnClickListener {
            showCustomDialog()
        }
        binding.searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                _folderAdapter.filter.filter(query)
                return false;
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                _folderAdapter.filter.filter(newText)
                return false;
            }
        })
    }

    fun dismissKeyboard(activity: Activity) {
        val inputMethodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusedView = activity.currentFocus
        if (currentFocusedView != null) {
            inputMethodManager.hideSoftInputFromWindow(
                currentFocusedView.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    private fun showCustomDialog() {
        val dialog = Dialog(requireContext())
        dialog.setCancelable(false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_add_note)
        val window: Window? = dialog.window;
        if (window == null) {
            return
        }
        window.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        val windowAtribute: WindowManager.LayoutParams = window.attributes
        windowAtribute.gravity = Gravity.CENTER
        window.attributes = windowAtribute
        val txtName = dialog.findViewById<TextView>(R.id.txt_name_note)
        val btnOk = dialog.findViewById<MaterialButton>(R.id.btnOkAddFol)
        val btnCancel = dialog.findViewById<MaterialButton>(R.id.btnHuyAddFol)
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        btnOk.setOnClickListener {
            try {
                DictionaryFolder.insertDictionaryFolder(
                    DictionaryFolder(
                        nameDictionaryFolder = txtName.text.toString(),
                        numDictionary = 0,
                        timeUpdate = Date().time,
                        idDictionaryFolder = 0
                    )
                )
                showToast(requireContext(), "them thanh cong")
                dialog.dismiss()
            } catch (e: Exception) {
                showToast(requireContext(), "them that bai")
                dialog.dismiss()
            }
        }
        dialog.show()
    }


}


