package com.example.myappnews.Ui.Fragment.management.Author

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2
import com.example.myappnews.Ui.Fragment.management.Admin.dismissAllKeyboards
import com.example.myappnews.Ui.Fragment.management.Author.AdaptPage.AuthorPageAdapter
import com.example.myappnews.databinding.AuthorHomeBinding
import com.google.android.material.tabs.TabLayout

class AuthorHome : Fragment() {
    private lateinit var binding: AuthorHomeBinding
    private var currentPosition = 0;
    private lateinit var tablayout: TabLayout;
    private lateinit var viewpager2: ViewPager2;
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AuthorHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayout()
        event(view)
    }

    override fun onResume() {
        super.onResume()
        tablayout.selectTab(tablayout.getTabAt(currentPosition))
        dismissAllKeyboards(requireActivity())
    }

    private fun initLayout() {
        tablayout = binding.tabLayoutAuthor;
        viewpager2 = binding.viewpagerAuthor;
        val adapter = AuthorPageAdapter(requireContext(), childFragmentManager, lifecycle)
        tablayout.addTab(tablayout.newTab().setText("Đã Đăng"))
        tablayout.addTab(tablayout.newTab().setText("Đang chờ xét duyệt"))
        tablayout.addTab(tablayout.newTab().setText("Bi từ chối"))
        tablayout.addTab(tablayout.newTab().setText("Yêu cầu chỉnh sửa"))
        viewpager2.adapter = adapter
        tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    viewpager2.currentItem = tab.position
                    currentPosition = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
//        viewpager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                tablayout.selectTab(tablayout.getTabAt(position));
//            }
//        })
        viewpager2.setUserInputEnabled(false)
    }

    private fun event(view: View) {
        binding.btnBackAuthor.setOnClickListener {
            Navigation.findNavController(view).popBackStack()
        }
    }
}