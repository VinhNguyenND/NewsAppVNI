package com.example.myappnews.Ui.Fragment.management.Admin

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2
import com.example.myappnews.Data.constant.dismissKeyboard
import com.example.myappnews.R
import com.example.myappnews.Ui.Fragment.management.Author.AdaptPage.AdminPageAdapter
import com.example.myappnews.databinding.MainAdmFragBinding
import com.google.android.material.tabs.TabLayout

class MainAdmFrag : Fragment() {
    private lateinit var binding: MainAdmFragBinding;
    private var currentPosition = 0;
    private lateinit var tablayout: TabLayout;
    private lateinit var viewpager2: ViewPager2;
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
        event(view)
    }

    override fun onResume() {
        super.onResume()
        tablayout.selectTab(tablayout.getTabAt(currentPosition))
        dismissAllKeyboards(requireActivity())
    }

    private fun navigate(id: Int) {
        Navigation.findNavController(binding.root).navigate(id)
    }

    private fun event(view: View) {
        binding.btnBackAdmin.setOnClickListener {
            Navigation.findNavController(view).popBackStack()
        }
    }


    private fun initLayout() {
        viewpager2 = binding.viewpagerAdmin
        tablayout = binding.tabLayoutAdmin
        val adapter = AdminPageAdapter(requireContext(), childFragmentManager, lifecycle)
        tablayout.addTab(tablayout.newTab().setText("Danh sách đã duyệt"))
        tablayout.addTab(tablayout.newTab().setText("Đang chờ xét duyệt"))
        tablayout.addTab(tablayout.newTab().setText("Đang chờ chỉnh sửa"))
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
//
//
//        })
        viewpager2.setUserInputEnabled(false)
    }
}

fun dismissAllKeyboards(activity: Activity) {
    val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
}