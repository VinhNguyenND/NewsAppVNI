package com.example.myappnews.Ui.Fragment.management.Author.AdaptPage

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myappnews.Ui.Fragment.management.Admin.ApprovedFragment
import com.example.myappnews.Ui.Fragment.management.Admin.AwaitRequireEdit.AwaitRequireEdit
import com.example.myappnews.Ui.Fragment.management.Admin.Await_Frag

class AdminPageAdapter(
    context: Context,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    private val _context = context
    override fun getItemCount(): Int {
        return 6;
    }

    override fun createFragment(position: Int): Fragment {
        var a: Fragment = ApprovedFragment();
        when (position) {
            1 -> {
                a = Await_Frag()
            }
            2->{
                a=AwaitRequireEdit()//nho set size
            }
        }
        return a;
    }
}