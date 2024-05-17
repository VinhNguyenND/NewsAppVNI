package com.example.myappnews.Ui.Fragment.Home

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.myappnews.Data.Api.Internet.InternetViewModel
import com.example.myappnews.Data.BroadCast.NetWorkChange.NetworkChangeListener
import com.example.myappnews.Data.BroadCast.NetWorkChange.NetworkChangeReceiver
import com.example.myappnews.Data.Firebase.ViewModel.ArticleViewModel.ArViewModel
import com.example.myappnews.Data.Local.Article.History.ArticlelocalViewModel
import com.example.myappnews.Data.Model.Article.Field
import com.example.myappnews.Data.Model.Article.NewsArticle
import com.example.myappnews.Data.Model.Source.Source
import com.example.myappnews.Interface.Adapter.CommonAdapter
import com.example.myappnews.R
import com.example.myappnews.R.*
import com.example.myappnews.Ui.Fragment.History.Article.AdapterPage.FragmentPageAdapter
import com.example.myappnews.Ui.Fragment.Home.Adapt.AdapterPage
import com.example.myappnews.Ui.Fragment.Home.Adapt.ArticleAdapter
import com.example.myappnews.Ui.Fragment.Home.Adapt.SourceAdapter
import com.example.myappnews.Ui.Fragment.Home.Adapt.popupAdapter
import com.example.myappnews.Ui.Fragment.management.Author.Home.showToast
import com.example.myappnews.databinding.HomeScreenBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout

class Home_Fragment : Fragment() {
    private lateinit var binding: HomeScreenBinding;
    private lateinit var _articleAdapter: ArticleAdapter
    private val ArticleViewModel = ArViewModel.getInstance();
    private lateinit var articlelocalViewModel: ArticlelocalViewModel
    private lateinit var internetViewModel: InternetViewModel
    private var listArticle = ArrayList<NewsArticle>()
    private var listField = ArrayList<Field>()
    private var listSource = ArrayList<Source>()
    private var topic = "All";
    private var source = "All";
    private lateinit var adapter: popupAdapter
    private lateinit var sourceAdapter: SourceAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeScreenBinding.inflate(inflater, container, false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        observeNetwork()
        initAdapter()
        initShimer()
        initRcView(requireParentFragment().requireContext())
        setEventForTop(view)
    }

    private fun initRcView(context: Context) {
        _articleAdapter = ArticleAdapter(listArticle, context)
        binding.homeRecycle.let {
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
                bundle.putInt("ishistory",1)
                Navigation.findNavController(binding.root).navigate(R.id.article_Fragment, bundle)
            }
        })
    }

    private fun observeNetwork() {
        internetViewModel.getChangeInternet().observe(viewLifecycleOwner, Observer { isConnected ->
            if (!isConnected) {
                binding.homeRecycle.visibility = View.GONE
                binding.shimmerViewContainer.visibility = View.GONE
                binding.imageNoInternetHome.visibility = View.VISIBLE
                binding.filterHome.visibility = View.GONE
            } else {
                binding.homeRecycle.visibility = View.VISIBLE
                binding.imageNoInternetHome.visibility = View.GONE
                binding.filterHome.visibility = View.VISIBLE
            }
        })
    }

    private fun initLayout() {
        binding.txtNguonHome.text = source;
        binding.txtChuDe.text = topic
    }

    override fun onResume() {
        super.onResume()
        getAllArticle(topic, source)
        initLayout()
        getAllField()
        getAllSource()

    }

    override fun onPause() {
        super.onPause()
        listArticle = ArrayList<NewsArticle>()
        listSource = ArrayList<Source>()
    }

    private fun getAllArticle(topic: String, source: String) {
        binding.shimmerViewContainer.startShimmer()
        ArticleViewModel.getNewByTopic(topic, source).observe(viewLifecycleOwner, Observer {
            listArticle = it;
            _articleAdapter.submitList(it);
            binding.shimmerViewContainer.stopShimmer()
            binding.shimmerViewContainer.hideShimmer()
            binding.shimmerViewContainer.visibility = View.GONE
        })
    }

    private fun getAllSource() {
        ArticleViewModel.getAllSource().observe(viewLifecycleOwner,
            Observer {
                listSource = it;
                sourceAdapter.submitList(listSource)
            })
    }

    private fun getAllField() {
        ArticleViewModel.getAllField().observe(
            viewLifecycleOwner, Observer {
                listField = it;
                adapter.submitList(listField);
            }
        )

    }

    private fun initShimer() {
        binding.shimmerViewContainer.visibility = View.VISIBLE
    }

    private fun initViewModel() {
        articlelocalViewModel = ViewModelProvider(this).get(ArticlelocalViewModel::class.java)
        internetViewModel = ViewModelProvider(this)[InternetViewModel::class.java]
    }

    private fun initAdapter() {
        adapter = popupAdapter(listField, requireContext())
        sourceAdapter = SourceAdapter(listSource, requireContext())
    }


    private fun showCustomDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layout.all_pop_up_window)
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
        val recyclerView = dialog.findViewById<RecyclerView>(R.id.allchoosepopuprc)
        dialog.findViewById<TextView>(R.id.btncloseallpop).setOnClickListener {
            dialog.cancel()
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter.setClickListener(object : CommonAdapter {
            @SuppressLint("SetTextI18n")
            override fun setOnClickListener(position: Int) {
                for (i in listField.indices) {
                    if (i != position) {
                        listField[i].choose = false
                    }
                }
                listField[position].choose = !listField[position].choose;
                adapter.submitList(listField);
                topic = listField[position].fieldId.toString();
                ArticleViewModel.getNewByTopic(topic, source);
                if (listField[position].fieldId!!.length > 4) {
                    binding.txtChuDe.text = listField[position].fieldId!!.substring(0, 4) + ".."
                } else {
                    binding.txtChuDe.text = listField[position].fieldId
                }
                dialog.dismiss()
            }
        })
        recyclerView.adapter = adapter
        dialog.show()

    }


    private fun showCustomDialogSource() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layout.all_pop_up_window)
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
        val recyclerView = dialog.findViewById<RecyclerView>(R.id.allchoosepopuprc)
        dialog.findViewById<TextView>(R.id.btncloseallpop).setOnClickListener {
            dialog.cancel()
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        sourceAdapter.setClickListener(object : CommonAdapter {
            @SuppressLint("SetTextI18n")
            override fun setOnClickListener(position: Int) {
                for (i in listSource.indices) {
                    if (i != position) {
                        listSource[i].choose = false
                    }
                }
                listSource[position].choose = !listSource[position].choose!!;
                sourceAdapter.submitList(listSource);
                source = listSource[position].SourceName.toString();
                ArticleViewModel.getNewByTopic(topic, source)
                if (listSource[position].SourceName!!.length > 4) {
                    binding.txtNguonHome.text =
                        listSource[position].SourceName!!.substring(0, 3) + ".."
                } else {
                    binding.txtNguonHome.text = listSource[position].SourceName
                }
                dialog.dismiss()
            }
        })
        recyclerView.adapter = sourceAdapter

        dialog.show()
    }


    private fun showBottomSheet() {
        val dialog = BottomSheetDialog(requireContext())
        val bottomshet = layoutInflater.inflate(layout.history_bottom_sheet, null)
        val btnXoatatca = bottomshet.findViewById<TextView>(R.id.xoatatcahome)
        val tablayout: TabLayout = bottomshet.findViewById<TabLayout>(R.id.tablayouthisHome);
        val viewpager2 = bottomshet.findViewById<ViewPager2>(R.id.historyHomepage);
        val adapter = FragmentPageAdapter(requireContext(), childFragmentManager, lifecycle, dialog)
        val close = bottomshet.findViewById<TextView>(R.id.DongHome)
        tablayout.addTab(tablayout.newTab().setText("Tin Tức"))
        btnXoatatca.setOnClickListener {
            articlelocalViewModel.deleteAllData()
        }
        close.setOnClickListener {
            dialog.dismiss()
        }
        viewpager2.adapter = adapter
        tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    viewpager2.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
        viewpager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                tablayout.selectTab(tablayout.getTabAt(position));
            }
        })
        dialog.setCancelable(true)
        dialog.setContentView(bottomshet!!)
        dialog.show()
    }

    @SuppressLint("MissingInflatedId")
    private fun showHistoryHeart() {
        val dialog = BottomSheetDialog(requireContext())
        val bottomSheet = layoutInflater.inflate(layout.history_bottom_sheet, null)
        val tittle = bottomSheet.findViewById<TextView>(R.id.xoatatcahome)
        val background = bottomSheet.findViewById<ConstraintLayout>(R.id.backgroundBottomSheet)
        val close = bottomSheet.findViewById<TextView>(R.id.DongHome)
        tittle.text = "Yêu thích";
        background.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white));
        val viewpager2 = bottomSheet.findViewById<ViewPager2>(R.id.historyHomepage);
        val adapter = AdapterPage(requireContext(), childFragmentManager, lifecycle, dialog);
        viewpager2.adapter = adapter
        close.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setCancelable(true)
        dialog.setContentView(bottomSheet!!)
        dialog.show()
    }

    private fun setEventForTop(view: View) {
        binding.NguonidHome.setOnClickListener {
            showCustomDialog()
        }
        binding.historyHome.setOnClickListener {
            showBottomSheet()
        }
        binding.sourceArticleHome.setOnClickListener {
            showCustomDialogSource()
        }
        binding.btnSearch.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.searchFragment)
        }
        binding.btnHeartHistory.setOnClickListener {
            showHistoryHeart()
        }
    }

}