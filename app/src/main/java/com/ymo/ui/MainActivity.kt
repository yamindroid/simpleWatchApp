package com.ymo.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.ymo.data.Resource
import com.ymo.data.Status
import com.ymo.data.model.api.Feed
import com.ymo.databinding.ActivityMainBinding
import com.ymo.ui.component.feed.FeedPagerAdapter
import com.ymo.ui.component.feed.FeedViewModel
import com.ymo.utils.showSnackbar
import com.ymo.utils.toGone
import com.ymo.utils.toVisible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: FeedViewModel by viewModels()

    private lateinit var feedPagerAdapter: FeedPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        feedPagerAdapter = FeedPagerAdapter(this, mutableListOf())
        binding.viewPager.adapter = feedPagerAdapter
        viewModel.feedsLiveData.observe(this, ::feedsHandler)

        // Load more data when user reaches the end of the list
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // Load more feeds when user reaches the end of the list
                if (position == feedPagerAdapter.itemCount - 1) {
                    viewModel.loadMoreFeeds()
                }
            }
        })
    }

    private fun feedsHandler(resource: Resource<List<Feed>>) {
        when (resource.status) {
            Status.LOADING -> showLoadingView()
            Status.SUCCESS -> resource.data?.let { feeds ->
                hideLoadingView()
                feedPagerAdapter.updateFeeds(feeds)
            }

            Status.ERROR -> {
                hideLoadingView()
                resource.errorMessage?.let { binding.root.showSnackbar(it, Snackbar.LENGTH_LONG) }
            }
        }
    }

    private fun hideLoadingView() {
        onHideImageSplash()
    }

    private fun showLoadingView() {
        onShowImageSplash()
    }

    private fun onShowImageSplash() {
        // Show image splash at first time app launch
        if (viewModel.getCurrentPage() > 1) return
        binding.imageSplash.toVisible()
    }

    private fun onHideImageSplash() {
        binding.imageSplash.toGone()
    }
}