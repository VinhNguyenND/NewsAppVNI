package com.example.myappnews.Ui.Fragment.History.Article.AdapterPage

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myappnews.Ui.Fragment.History.Article.ArticlehisFragment
import com.example.myappnews.Ui.Fragment.History.Dictionary.DictionaryHisFragment
import com.google.android.material.bottomsheet.BottomSheetDialog

class FragmentPageAdapter(
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
        return ArticlehisFragment(_context,_bottomSheet)
    }

}