package com.example.myappnews.Ui.Fragment.Home.heart

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myappnews.Data.Api.Internet.InternetViewModel
import com.example.myappnews.Data.Firebase.ViewModel.ArticleViewModel.ArViewModel
import com.example.myappnews.Data.Model.Article.NewsArticle
import com.example.myappnews.Interface.Adapter.CommonAdapter
import com.example.myappnews.R
import com.example.myappnews.Ui.Fragment.Home.Adapt.ArticleAdapter
import com.example.myappnews.databinding.HeartHistoryFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class HeartFragment(context: Context, bottomSheet: BottomSheetDialog) : Fragment() {
    private lateinit var binding: HeartHistoryFragmentBinding
    private lateinit var internetViewModel: InternetViewModel
    private val ArticleViewModel = ArViewModel.getInstance();
    private var listArticle = ArrayList<NewsArticle>()
    private lateinit var _articleAdapter: ArticleAdapter
    private val _bottomSheet = bottomSheet
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HeartHistoryFragmentBinding.inflate(inflater, container, false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initRcView(requireContext());
        observeNetwork()
    }

    override fun onResume() {
        super.onResume()
        ArticleViewModel.getHeart().observe(viewLifecycleOwner, Observer {
            listArticle = it;
            _articleAdapter.submitList(listArticle)
        })
    }

    private fun observeNetwork() {
        internetViewModel.getChangeInternet().observe(viewLifecycleOwner, Observer { isConnected ->
            if (!isConnected) {
                binding.layoutHeart.visibility = View.GONE
                binding.imageNoInternetHeart.visibility = View.VISIBLE
            } else {
                binding.layoutHeart.visibility = View.VISIBLE
                binding.imageNoInternetHeart.visibility = View.GONE
            }
        })
    }

    private fun initViewModel() {
        internetViewModel = ViewModelProvider(this)[InternetViewModel::class.java]
    }

    private fun initRcView(context: Context) {
        _articleAdapter = ArticleAdapter(listArticle, context)
        binding.rcvHeart.let {
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
                bundle.putInt("ishistory", 0)
                _bottomSheet.dismiss()
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                    .navigate(R.id.article_Fragment, bundle);
            }
        })
    }


}