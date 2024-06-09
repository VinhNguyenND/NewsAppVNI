package com.example.myappnews.Ui.Fragment.management.Admin

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
import com.example.myappnews.Data.Firebase.ViewModel.AdminViewModel.AdminViewModel
import com.example.myappnews.Data.Model.Article.NewsArticle
import com.example.myappnews.Interface.Adapter.CommonAdapter
import com.example.myappnews.R
import com.example.myappnews.Ui.Fragment.Home.Adapt.ArticleAdapter
import com.example.myappnews.Ui.Fragment.management.Author.Home.showToast
import com.example.myappnews.databinding.HomeAdminBinding

//dang cho xet duyet
class Await_Frag : Fragment() {
    private lateinit var binding: HomeAdminBinding
    private val _adminViewModel = AdminViewModel.getInstance()
    private lateinit var rcvHomeAdapter: ArticleAdapter
    private var listArticle = ArrayList<NewsArticle>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initShimer()
        initRcView(requireContext())
    }

    override fun onResume() {
        super.onResume()
        getAllArticle()
    }

    private fun initRcView(context: Context) {
        rcvHomeAdapter = ArticleAdapter(listArticle, context)
        binding.rcvhomeadmin.let {
            rcvHomeAdapter.setIsRequire(true)
            it.adapter = rcvHomeAdapter
            it.layoutManager = LinearLayoutManager(
                parentFragment?.requireContext(),
                RecyclerView.VERTICAL, false
            )
        }
        rcvHomeAdapter.setClickListener(object : CommonAdapter {
            override fun setOnClickListener(position: Int) {
                val bundle = Bundle()
                bundle.putParcelable("Article", listArticle[position])
                Navigation.findNavController(binding.root).navigate(R.id.editFragment, bundle)
            }
        })
    }

    private fun getAllArticle() {
        binding.shimmerViewContainer.startShimmer()
        _adminViewModel.getAllAwait(0).observe(viewLifecycleOwner, Observer {
            listArticle = it;
            rcvHomeAdapter.submitList(it);
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