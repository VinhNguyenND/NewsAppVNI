package com.example.myappnews.Ui.Fragment.Search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myappnews.Data.Local.Dictionary.Entity.DictionaryFolder
import com.example.myappnews.Data.Local.Dictionary.Helper.DictionaryViewModel
import com.example.myappnews.Interface.Adapter.CommonAdapter
import com.example.myappnews.Ui.Fragment.Search.Adapt.FolderAdapter
import com.example.myappnews.databinding.SearchScreenBinding

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
        listFolder = arrayListOf(
            DictionaryFolder(1, "Folder 1", System.currentTimeMillis(), 10),
            DictionaryFolder(2, "Folder 2", System.currentTimeMillis(), 20),
            DictionaryFolder(3, "Folder 3", System.currentTimeMillis(), 30),
            DictionaryFolder(4, "Folder 4", System.currentTimeMillis(), 40),
            DictionaryFolder(5, "Folder 5", System.currentTimeMillis(), 50)
        )
        initRcView(requireContext())
    }

    override fun onResume() {
        super.onResume()
        DictionaryFolder.readAllDicFolder.observe(
            viewLifecycleOwner,
            Observer {
                for (doc in it) {
                    listFolder.add(doc)
                }
                _folderAdapter.submitList(listFolder)
            }
        )
    }

    private fun initViewModel() {
        DictionaryFolder = ViewModelProvider(this).get(DictionaryViewModel::class.java)
    }

    private fun initRcView(context: Context) {
        _folderAdapter = FolderAdapter(listFolder, context)
        binding.rcvDictionary.let {
            it.adapter = _folderAdapter
            it.layoutManager = LinearLayoutManager(
                parentFragment?.requireContext(),
                RecyclerView.VERTICAL, false
            )
        }
        _folderAdapter.setClickListener(object : CommonAdapter {
            override fun setOnClickListener(position: Int) {
//                val bundle = Bundle()
//                bundle.putParcelable("Article", listArticle[position])
//                Navigation.findNavController(binding.root).navigate(R.id.article_Fragment, bundle)
            }
        })
    }

    private fun Add_Dic(dictionaryFolder: DictionaryFolder) {
        binding.idAddDic.setOnClickListener {
            DictionaryFolder.insertDictionaryFolder(dictionaryFolder)
        }
    }

    private fun showDialogAdd() {

    }
}