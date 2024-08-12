package com.ymo.ui.component.feed

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ymo.data.model.api.Feed
import com.ymo.databinding.ActivityFeedBinding
import com.ymo.databinding.FragmentFeedBinding
import com.ymo.ui.component.now_playing.adapter.CommentAdapter
import com.ymo.utils.loadFromUrl

//class FeedFragment : Fragment() {
//
//    private lateinit var binding: ActivityFeedBinding
//    private lateinit var viewModel: FeedViewModel
//    private lateinit var feedPagingDataAdapter: FeedPagingDataAdapter
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = ActivityFeedBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // Calculate screen height for paging
//        val screenHeight = getScreenHeight(requireContext())
//
//        // Initialize PagingDataAdapter
//        feedPagingDataAdapter = FeedPagingDataAdapter { feed ->
//            // Handle item click
//        }
//
//        // Initialize and set LayoutManager
//        val verticalPagingLayoutManager = VerticalPagingLayoutManager(requireContext(), screenHeight)
//        binding.recyclerView.layoutManager = verticalPagingLayoutManager
//      //  verticalPagingLayoutManager.setRecyclerView(binding.recyclerView)
//        binding.recyclerView.adapter = feedPagingDataAdapter
//
//        // Observe PagingData from ViewModel
//        viewModel.feedsLiveData.observe(viewLifecycleOwner) { pagingData ->
//            feedPagingDataAdapter.submitData(lifecycle, pagingData)
//        }
//    }
//
//    private fun getScreenHeight(context: Context): Int {
//        val displayMetrics = context.resources.displayMetrics
//        return displayMetrics.heightPixels
//    }
//}
//

class FeedFragment : Fragment() {

    private lateinit var binding: FragmentFeedBinding
    private lateinit var feed: Feed
    private lateinit var gestureDetector: GestureDetectorCompat
    private lateinit var commentsAdapter: CommentAdapter
    private var areCommentsShowing = true

    companion object {
        private const val ARG_FEED = "arg_feed"

        fun newInstance(feed: Feed): FeedFragment {
            val fragment = FeedFragment()
            val args = Bundle().apply {
                putParcelable(ARG_FEED, feed)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        commentsAdapter = CommentAdapter()
        binding.commentsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.commentsRecyclerView.adapter = commentsAdapter

        arguments?.getParcelable<Feed>(ARG_FEED)?.let {
            feed = it
            binding.feedImage.loadFromUrl(it.imagefile?.first())
            binding.avatarImage.loadFromUrl(it.avatar)
            binding.feed = it
            it.topcomments?.let { comments ->
                commentsAdapter.submitList(comments)
            }
        }

        // Initialize GestureDetector for swipe detection
        gestureDetector = GestureDetectorCompat(requireContext(), SwipeGestureListener())

//        // Set onTouchListener to handle swipe gestures
//        binding.container.setOnTouchListener { _, event ->
//            gestureDetector.onTouchEvent(event)
//            true
//        }
        setOnTouchListenerForViews(binding.container, binding.commentsRecyclerView)
    }

    private fun setOnTouchListenerForViews(vararg views: View) {
        for (view in views) {
            view.setOnTouchListener { _, event ->
                gestureDetector.onTouchEvent(event)
                true
            }
        }
    }

    private inner class SwipeGestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            if (Math.abs(distanceY) > Math.abs(distanceX)) {
                // Vertical swipe
                if (distanceY > 0) {
                    // Swipe down
                    // Optionally, handle the action for swipe down
                } else {
                    // Swipe up
                    // Optionally, handle the action for swipe up
                }
            } else {
                // Horizontal swipe
                if (distanceX > 0) {
                    // Swipe left
                    showComments()
                } else {
                    // Swipe right
                    hideComments()
                }
            }
            return true
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            // Handle single tap (if needed)
            // This can be used to hide comments if a tap is detected
            if (areCommentsShowing) {
                hideComments()
            }
            return super.onSingleTapUp(e)
        }

        private fun showComments() {
            if(feed.topcomments.isNullOrEmpty()) return

            // reset position 
            binding.commentsRecyclerView.translationX = binding.commentsRecyclerView.width.toFloat()
            binding.commentsRecyclerView.alpha = 0f
            Log.e("////", "showComments: binding.commentsContainer.width.toFloat() ${binding.commentsRecyclerView.width.toFloat()}", )
            binding.commentsRecyclerView.animate()
                .alpha(1f)
                .translationX(0f)
                .setDuration(300)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator) {
                        super.onAnimationStart(animation)
                        binding.overlay.animate()
                            .alpha(1f)
                            .setDuration(300)
                    }
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        areCommentsShowing = true
                        binding.commentsRecyclerView.visibility = View.VISIBLE // Set visibility to GONE after animation ends
                        binding.overlay.visibility = View.VISIBLE
                    }
                })
        }

        private fun hideComments() {
            binding.commentsRecyclerView.animate()
                .alpha(0f)
                .translationX(binding.commentsRecyclerView.width.toFloat())
                .setDuration(300)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator) {
                        super.onAnimationStart(animation)
                        binding.overlay.animate()
                            .alpha(0f)
                            .setDuration(300)
                    }
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        areCommentsShowing = false
                        // binding.commentsRecyclerView.visibility = View.VISIBLE
                        // here overlay view will not be hide in order to smooth animation
                    }
                })
        }
    }
}