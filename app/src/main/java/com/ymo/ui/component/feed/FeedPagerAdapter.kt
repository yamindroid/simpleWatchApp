package com.ymo.ui.component.feed

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ymo.data.model.api.Feed

class FeedPagerAdapter(
    activity: FragmentActivity, private var feeds: MutableList<Feed>
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = feeds.size

    override fun createFragment(position: Int): Fragment {
        return FeedFragment.newInstance(feeds[position])
    }

    fun updateFeeds(newFeeds: List<Feed>) {
        // Handle not to show old feeds again if there is no internet
        if (feeds.containsAll(newFeeds)) return

        feeds.addAll(newFeeds)
        notifyDataSetChanged()
    }
}