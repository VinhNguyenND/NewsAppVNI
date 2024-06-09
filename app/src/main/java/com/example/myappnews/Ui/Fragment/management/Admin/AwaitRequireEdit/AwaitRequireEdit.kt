package com.example.myappnews.Ui.Fragment.management.Admin.AwaitRequireEdit

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.example.myappnews.Ui.Fragment.management.Admin.AwaitRequireEdit.Adapter.AwaitRequireEditAdapter
import com.example.myappnews.databinding.AwaitRequireEditBinding

//danh sach dang cho phan hoi chinh sua tu user
class AwaitRequireEdit : Fragment() {
    private lateinit var binding: AwaitRequireEditBinding
    private val _adminViewModel = AdminViewModel.getInstance()
    private lateinit var rcvAdapter: AwaitRequireEditAdapter
    private var listArticle = ArrayList<NewsArticle>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AwaitRequireEditBinding.inflate(inflater, container, false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initShimer()
        initRcView(requireContext());
        observe()
    }

    override fun onResume() {
        super.onResume()
        getAllArticle()
    }

    private fun observe(){
        _adminViewModel.observerGetNewsAwaitEdit().observe(viewLifecycleOwner, Observer {
            listArticle = it;
            rcvAdapter.submitList(it);
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

    private fun initRcView(context: Context) {
        rcvAdapter = AwaitRequireEditAdapter(listArticle, context)
        binding.rcvAwaitEditAdm.let {
            it.adapter = rcvAdapter
            it.layoutManager = LinearLayoutManager(
                parentFragment?.requireContext(),
                RecyclerView.VERTICAL, false
            )
        }
        rcvAdapter.setClickListener(object : CommonAdapter {
            override fun setOnClickListener(position: Int) {
                val bundle = Bundle()
                bundle.putParcelable("Article", listArticle[position])
                Navigation.findNavController(binding.root).navigate(R.id.awaitRqEdit, bundle)
            }
        })
    }

    private fun getAllArticle() {
        binding.shimmerViewContainer.startShimmer()
        _adminViewModel.getNewsAwaitEdit()
    }

    private fun initShimer() {
        binding.shimmerViewContainer.visibility = View.VISIBLE
    }
}