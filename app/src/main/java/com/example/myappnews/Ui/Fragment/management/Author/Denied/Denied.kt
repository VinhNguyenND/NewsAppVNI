package com.example.myappnews.Ui.Fragment.management.Author.Denied

import android.content.Context
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
import com.example.myappnews.databinding.DeniedAuthorBinding

class Denied : Fragment() {
    private lateinit var binding: DeniedAuthorBinding
    private val _authorViewModel = AuthorViewModel.getInstance()
    private lateinit var _articleAdapter: ArticleAdapter
    private var listArticle = ArrayList<NewsArticle>()
    private lateinit var _shared_Preference: Shared_Preference;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DeniedAuthorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initShimer()
        _shared_Preference = Shared_Preference(requireActivity());
        initRcView(requireContext())
    }

    override fun onResume() {
        super.onResume()
        initData()
    }

    private fun initRcView(context: Context) {
        _articleAdapter = ArticleAdapter(listArticle, context)
        binding.rcvDeniedAuthor.let {
            _articleAdapter.setIsRequire(true)
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
                Navigation.findNavController(binding.root).navigate(R.id.editDenied, bundle)
            }
        })
    }

    private fun initData() {
        binding.shimmerViewContainer.startShimmer()
        _authorViewModel.getAllDenied(_shared_Preference.getUid().toString())
            .observe(viewLifecycleOwner, Observer {
                listArticle = it;
                _articleAdapter.submitList(it);
                if (it.isNotEmpty()) {
                    binding.shimmerViewContainer.stopShimmer()
                    binding.shimmerViewContainer.hideShimmer()
                    binding.shimmerViewContainer.visibility = View.GONE
                    binding.noDataFound.visibility = View.GONE
                } else {
                    binding.shimmerViewContainer.stopShimmer()
                    binding.shimmerViewContainer.hideShimmer()
                    binding.shimmerViewContainer.visibility = View.GONE
                    binding.noDataFound.visibility = View.VISIBLE
                }
            })
    }

    private fun initShimer() {
        binding.shimmerViewContainer.visibility = View.VISIBLE
    }

}