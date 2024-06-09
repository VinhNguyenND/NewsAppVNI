package com.example.myappnews.Ui.Fragment.Article.Note

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myappnews.Data.Local.Dictionary.Entity.DictionaryFolder
import com.example.myappnews.Data.Local.Dictionary.Entity.DictionaryItem
import com.example.myappnews.Data.Local.Dictionary.Helper.DictionaryViewModel
import com.example.myappnews.Interface.Adapter.CommonAdapter
import com.example.myappnews.R
import com.example.myappnews.Ui.Fragment.note_dic.Adapt.FolderAdapter
import com.example.myappnews.Ui.Fragment.management.Author.Home.showToast
import com.example.myappnews.databinding.AddNoteLayoutBinding
import com.google.android.material.button.MaterialButton
import java.util.Date

class AddNote : Fragment() {
    private lateinit var binding: AddNoteLayoutBinding
    private lateinit var DictionaryFolder: DictionaryViewModel
    private lateinit var _folderAdapter: FolderAdapter
    private var listFolder = ArrayList<DictionaryFolder>()
    private lateinit var dictionary: DictionaryItem
    private var isSort = false;
    private lateinit var observer: Observer<List<DictionaryFolder>>
    private lateinit var observer1: Observer<List<DictionaryFolder>>
    private lateinit var observer2: Observer<List<DictionaryFolder>>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddNoteLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initObserve()
        initRcView(requireContext())
        initContent()
        event()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun initContent() {
        if (arguments?.getParcelable("dictionaryItem", DictionaryItem::class.java) != null) {
            dictionary = arguments?.getParcelable("dictionaryItem", DictionaryItem::class.java)!!
            showToast(requireContext(), dictionary.word)
        }
    }

    override fun onResume() {
        super.onResume()
        DictionaryFolder.readAllDicFolder.observe(
            viewLifecycleOwner,
            observer
        )
    }

    private fun initObserve() {
        observer = Observer {
            listFolder = ArrayList<DictionaryFolder>()
            for (doc in it) {
                listFolder.add(doc)
            }
            _folderAdapter.submitList(listFolder)
        }

        observer1 = Observer {
            isSort = true;
            listFolder = ArrayList<DictionaryFolder>()
            for (doc in it) {
                listFolder.add(doc)
            }
            _folderAdapter.submitList(listFolder)
        }

        observer2 = Observer {
            isSort = false;
            listFolder = ArrayList<DictionaryFolder>()
            for (doc in it) {
                listFolder.add(doc)
            }
            _folderAdapter.submitList(listFolder)
        }
    }

    private fun initViewModel() {
        DictionaryFolder = ViewModelProvider(this).get(DictionaryViewModel::class.java)
    }

    private fun event() {
        binding.btnSort.setOnClickListener {
            if (!isSort) {
                DictionaryFolder.readAllDicFolder.removeObserver(observer)
                DictionaryFolder.getFolderSortDecrease().removeObserver(observer2)
                binding.btnSort.setImageResource(R.drawable.ic_a_to_z_24);
                DictionaryFolder.getFolderSortIncrease().observe(
                    viewLifecycleOwner,
                    observer1
                )
            } else {
                DictionaryFolder.readAllDicFolder.removeObserver(observer)
                DictionaryFolder.getFolderSortIncrease().removeObserver(observer1)
                binding.btnSort.setImageResource(R.drawable.ic_z_a_24);
                DictionaryFolder.getFolderSortDecrease().observe(
                    viewLifecycleOwner,
                    observer2
                )
            }
        };
        binding.btnAddFolder.setOnClickListener {
            showCustomDialog()
        }
        binding.searchView2.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                _folderAdapter.filter.filter(query)
                return false;
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                _folderAdapter.filter.filter(newText)
                return false;
            }
        })
        binding.btnback.setOnClickListener {
            binding.root.findNavController().popBackStack()
        }
    }

    private fun initRcView(context: Context) {
        _folderAdapter = FolderAdapter(listFolder, context, ViewModelProvider(this))
        binding.rcvFolderAddNote.let {
            it.adapter = _folderAdapter
            it.layoutManager = LinearLayoutManager(
                parentFragment?.requireContext(),
                RecyclerView.VERTICAL, false
            )
        }
        _folderAdapter.setClickListener(object : CommonAdapter {
            override fun setOnClickListener(position: Int) {
                try {
                    DictionaryFolder.insertDictionaryItem(
                        DictionaryItem(
                            idDictionaryFolder = listFolder[position].idDictionaryFolder,
                            idDictionaryItem = 0,
                            phonetic = dictionary.phonetic,
                            mean = dictionary.mean,
                            word = dictionary.word,
                            audio = dictionary.audio,
                            wordMean = dictionary.wordMean
                        )
                    )
                    showToast(requireContext(), "them thanh cong")
                } catch (e: Exception) {
                    Log.d("loi local data", e.toString())
                }
            }
        })
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