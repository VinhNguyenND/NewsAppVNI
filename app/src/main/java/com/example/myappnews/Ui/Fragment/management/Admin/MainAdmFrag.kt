package com.example.myappnews.Ui.Fragment.management.Admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2
import com.example.myappnews.R
import com.example.myappnews.Ui.Fragment.management.Author.AdaptPage.AdminPageAdapter
import com.example.myappnews.databinding.MainAdmFragBinding
import com.google.android.material.tabs.TabLayout

class MainAdmFrag : Fragment() {
    private lateinit var binding: MainAdmFragBinding;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainAdmFragBinding.inflate(inflater, container, false);
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayout()
//        event()
    }

    private fun navigate(id: Int) {
        Navigation.findNavController(binding.root).navigate(id)
    }

//    private fun event() {
//        binding.btnDanhSachBaiCanDuyet.setOnClickListener {
//            navigate(R.id.homeAdmFrag)
//        }
//        binding.btnDanhSachBaiDaDuyet.setOnClickListener {
//            navigate(R.id.approvedFragment)
//        }
//        binding.btnback.setOnClickListener {
//             Navigation.findNavController(binding.root).popBackStack()
//        }
//    }

    private fun initLayout() {
        val tablayout = binding.tabLayoutAdmin;
        val viewpager2 = binding.viewpagerAdmin;
        val adapter = AdminPageAdapter(requireContext(), childFragmentManager, lifecycle)
        tablayout.addTab(tablayout.newTab().setText("Danh sách đã duyệt"))
        tablayout.addTab(tablayout.newTab().setText("Đang chờ xét duyệt"))
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
    }
}