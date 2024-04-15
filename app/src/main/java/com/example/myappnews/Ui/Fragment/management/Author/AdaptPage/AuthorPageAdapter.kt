package com.example.myappnews.Ui.Fragment.management.Author.AdaptPage

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myappnews.Ui.Fragment.management.Author.AwaitingApproval.Awaiting
import com.example.myappnews.Ui.Fragment.management.Author.Denied.Denied
import com.example.myappnews.Ui.Fragment.management.Author.EditRequest.Request
import com.example.myappnews.Ui.Fragment.management.Author.Home.HomeAuthor

class AuthorPageAdapter(
    context: Context,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    private val _context = context
    override fun getItemCount(): Int {
        return 6;
    }

    override fun createFragment(position: Int): Fragment {
        var a: Fragment = HomeAuthor();
        when (position) {
            1 -> {
                a = Awaiting()
            }
            2 -> {
                a = Denied()
            }

            3 -> {
                a = Request()
            }
        }
        return a;
    }


}