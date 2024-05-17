package com.example.myappnews.Ui.Fragment.Home.Adapt

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myappnews.Ui.Fragment.History.Article.ArticlehisFragment
import com.example.myappnews.Ui.Fragment.Home.heart.HeartFragment
import com.google.android.material.bottomsheet.BottomSheetDialog

class AdapterPage(
    context: Context,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    bottomSheet: BottomSheetDialog
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    private val _context = context
    private val _bottomSheet=bottomSheet
    override fun getItemCount(): Int {
        return 1;
    }

    override fun createFragment(position: Int): Fragment {
        return HeartFragment(_context, bottomSheet =_bottomSheet )
    }

}