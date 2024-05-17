package com.example.myappnews.Ui.Fragment.management.Author.EditRequest

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myappnews.Data.Firebase.ViewModel.AuthorViewModel.AuthorViewModel
import com.example.myappnews.Data.Model.Article.NewsArticle
import com.example.myappnews.Data.SharedPreferences.Shared_Preference
import com.example.myappnews.Interface.Adapter.CommonAdapter
import com.example.myappnews.R
import com.example.myappnews.Ui.Fragment.Home.Adapt.ArticleAdapter
import com.example.myappnews.databinding.EditRequestAuthorBinding
import org.apache.poi.xwpf.usermodel.XWPFDocument

class Request : Fragment() {
    private lateinit var binding: EditRequestAuthorBinding
    private val _authorViewModel = AuthorViewModel.getInstance()
    private lateinit var _articleAdapter: ArticleAdapter
    private var listArticle = ArrayList<NewsArticle>()
    private lateinit var _shared_Preference: Shared_Preference;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = EditRequestAuthorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _shared_Preference = Shared_Preference(requireActivity());
        initRcView(requireContext())
    }

    override fun onResume() {
        super.onResume()
        initData()
    }

    private fun initRcView(context: Context) {
        _articleAdapter = ArticleAdapter(listArticle, context)
        binding.rcvRequestAuthor.let {
            it.adapter = _articleAdapter
            it.layoutManager = LinearLayoutManager(
                parentFragment?.requireContext(),
                RecyclerView.VERTICAL, false
            )
        }
        _articleAdapter.setClickListener(object : CommonAdapter {
            override fun setOnClickListener(position: Int) {
                val bundle = Bundle()
                bundle.putParcelable("Article", listArticle[position])
                Navigation.findNavController(binding.root).navigate(R.id.requestEdit, bundle)
            }
        })
    }

    private fun initData() {
        _authorViewModel.getAllRequireEdit(_shared_Preference.getUid().toString())
            .observe(viewLifecycleOwner, Observer {
                listArticle = it;
                _articleAdapter.submitList(it);
            })
    }


}