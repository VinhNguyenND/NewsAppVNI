package com.example.myappnews.Ui.Fragment.History.Article.AdapterPage

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myappnews.Ui.Fragment.History.Article.ArticlehisFragment
import com.example.myappnews.Ui.Fragment.History.Dictionary.DictionaryHisFragment

class FragmentPageAdapter(
    context: Context,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    private val _context = context
    override fun getItemCount(): Int {
        return 2;
    }

    override fun createFragment(position: Int): Fragment {
        return if (position == 0)
            ArticlehisFragment(_context)
        else
            DictionaryHisFragment()
    }


}