package com.example.myappnews.Ui.Fragment.Home

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.Window
import android.view.WindowManager
import android.widget.TableLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.myappnews.Data.FakeData.ArticleStore
import com.example.myappnews.Data.Firebase.ViewModel.ArticleViewModel.ArViewModel
import com.example.myappnews.Data.Local.Article.ArticlelocalViewModel
import com.example.myappnews.Data.Model.Article.Article
import com.example.myappnews.Data.Model.Article.NewsArticle
import com.example.myappnews.Data.Model.item.Field
import com.example.myappnews.Interface.Adapter.CommonAdapter
import com.example.myappnews.R
import com.example.myappnews.Ui.Fragment.History.Article.AdapterPage.FragmentPageAdapter
import com.example.myappnews.Ui.Fragment.Home.Adapt.ArticleAdapter
import com.example.myappnews.Ui.Fragment.Home.Adapt.popupAdapter
import com.example.myappnews.databinding.HomeScreenBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.google.android.play.integrity.internal.ad
import org.w3c.dom.Text

class Home_Fragment : Fragment() {
    private lateinit var binding: HomeScreenBinding;
    private lateinit var _articleAdapter: ArticleAdapter
    private lateinit var _articleHistory:ArticleAdapter;
    private val ArticleViewModel = ArViewModel.getInstance();
    private lateinit var articlelocalViewModel: ArticlelocalViewModel
    private var listArticle = ArrayList<NewsArticle>()
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
        initShimer()
        initRcView(requireParentFragment().requireContext())
        setEventForTop()
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
                Navigation.findNavController(binding.root).navigate(R.id.article_Fragment, bundle)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        getAllArticle()
    }

    override fun onPause() {
        super.onPause()
        listArticle = ArrayList<NewsArticle>()
    }

    private fun getAllArticle() {
        binding.shimmerViewContainer.startShimmer()
        ArticleViewModel.getAllArticle().observe(viewLifecycleOwner, Observer {
            listArticle = it;
            _articleAdapter.submitList(it);
            binding.shimmerViewContainer.stopShimmer()
            binding.shimmerViewContainer.hideShimmer()
            binding.shimmerViewContainer.visibility = View.GONE
        })
    }

    private fun initShimer() {
        binding.shimmerViewContainer.visibility = View.VISIBLE
    }
    private fun initViewModel(){
       articlelocalViewModel= ViewModelProvider(this).get(ArticlelocalViewModel::class.java)
    }

    @SuppressLint("ResourceType")
    private fun showCustomDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.all_pop_up_window)
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

        val adapter = popupAdapter(getdatachooseall(), requireContext())
        adapter.setClickListener(object : CommonAdapter {
            override fun setOnClickListener(position: Int) {
                Log.i("click", position.toString())
            }
        })

        recyclerView.adapter = adapter


        dialog.show()
    }


    private fun showBottmSheet() {
        val dialog = BottomSheetDialog(requireContext())
        val bottomshet= layoutInflater.inflate(R.layout.history_bottom_sheet, null)
        val btnXoatatca=bottomshet.findViewById<TextView>(R.id.xoatatcahome)
        val tablayout: TabLayout =bottomshet.findViewById<TabLayout>(R.id.tablayouthisHome);
        val viewpager2=bottomshet.findViewById<ViewPager2>(R.id.historyHomepage);
        val adapter=FragmentPageAdapter(requireContext(),childFragmentManager,lifecycle)
        tablayout.addTab(tablayout.newTab().setText("Tin Tức"))
        tablayout.addTab(tablayout.newTab().setText("Từ Vựng"))
        btnXoatatca.setOnClickListener {
            articlelocalViewModel.deleteAllData()
        }
        viewpager2.adapter=adapter
        tablayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    viewpager2.currentItem=tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
        viewpager2.registerOnPageChangeCallback(object:ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
              tablayout.selectTab(tablayout.getTabAt(position));
            }
        } )
        dialog.setCancelable(true)
        dialog.setContentView(bottomshet!!)
        dialog.show()
    }

    private fun getdatachooseall(): ArrayList<Field> {
        val fieldList = ArrayList<Field>()
        fieldList.add(Field("Field 1", false))
        fieldList.add(Field("Field 2", false))
        fieldList.add(Field("Field 3", false))
        fieldList.add(Field("Field 4", false))
        fieldList.add(Field("Field 5", false))
        fieldList.add(Field("Field 1", false))
        fieldList.add(Field("Field 2", false))
        fieldList.add(Field("Field 3", false))
        fieldList.add(Field("Field 4", false))
        fieldList.add(Field("Field 5", false))
        fieldList.add(Field("Field 1", false))
        fieldList.add(Field("Field 2", false))
        fieldList.add(Field("Field 3", false))
        fieldList.add(Field("Field 4", false))
        fieldList.add(Field("Field 5", false))
        return fieldList
    }

    private fun setEventForTop() {
        binding.NguonidHome.setOnClickListener {
            showCustomDialog()
        }
        binding.historyHome.setOnClickListener {
            showBottmSheet()
        }
    }

}