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
import com.example.myappnews.Data.SharedPreferences.Shared_Preference
import com.example.myappnews.Interface.Adapter.CommonAdapter
import com.example.myappnews.R
import com.example.myappnews.Ui.Fragment.Home.Adapt.ArticleAdapter
import com.example.myappnews.databinding.ApprovedFragmentBinding

//danh sach da xet duyet
class ApprovedFragment : Fragment() {
    private lateinit var _shared_Preference: Shared_Preference;
    private lateinit var binding: ApprovedFragmentBinding
    private val _adminViewModel = AdminViewModel.getInstance()
    private lateinit var rcvHomeAdapter: ArticleAdapter
    private var listArticle = ArrayList<NewsArticle>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ApprovedFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _shared_Preference = Shared_Preference(requireActivity());
        initShimer()
        initRcView(requireContext())
    }

    override fun onResume() {
        super.onResume()
        getAllArticle()
    }

    private fun initRcView(context: Context) {
        rcvHomeAdapter = ArticleAdapter(listArticle, context)
        binding.rcvApprovedAdmin.let {
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
                Navigation.findNavController(binding.root).navigate(R.id.editApprovedFrg, bundle)
            }
        })
    }

    private fun getAllArticle() {
        binding.shimmerViewContainer.startShimmer()
        _shared_Preference.getUid()?.let {
            _adminViewModel.getAllApprove(1, it).observe(viewLifecycleOwner, Observer {
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
    }

    private fun initShimer() {
        binding.shimmerViewContainer.visibility = View.VISIBLE
    }


}