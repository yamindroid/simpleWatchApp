package com.ymo.ui.component.feed

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ymo.data.model.api.Feed
import com.ymo.databinding.FragmentFeedBinding
import com.ymo.ui.component.now_playing.adapter.CommentAdapter
import com.ymo.utils.loadFromUrl
import com.ymo.utils.toVisible

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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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
            binding.apply {
                feedImage.loadFromUrl(it.imageFile?.first())
                avatarImage.loadFromUrl(it.avatar)
                feed = it
            }
            it.topComments?.let { comments ->
                commentsAdapter.submitList(comments)
            }
        }

        // Initialize GestureDetector for swipe detection
        gestureDetector = GestureDetectorCompat(requireContext(), SwipeGestureListener())
        setOnTouchListenerForViews(binding.container, binding.commentsRecyclerView)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setOnTouchListenerForViews(vararg views: View) {
        val onTouchListener = View.OnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }

        views.forEach { view ->
            view.setOnTouchListener(onTouchListener)
        }
    }

    private inner class SwipeGestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onScroll(
            e1: MotionEvent?, e2: MotionEvent, distanceX: Float, distanceY: Float
        ): Boolean {
            if (!(Math.abs(distanceY) > Math.abs(distanceX))) {
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
            if (areCommentsShowing) {
                hideComments()
            }
            return super.onSingleTapUp(e)
        }

        private fun showComments() {
            if (feed.topComments.isNullOrEmpty()) return

            // Reset position
            binding.commentsRecyclerView.translationX = binding.commentsRecyclerView.width.toFloat()
            binding.commentsRecyclerView.alpha = 0f

            binding.commentsRecyclerView.animate().alpha(1f).translationX(0f).setDuration(300)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator) {
                        super.onAnimationStart(animation)
                        binding.overlay.animate().alpha(1f).setDuration(300)
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        areCommentsShowing = true
                        binding.commentsRecyclerView.toVisible()
                        binding.overlay.toVisible()
                    }
                })
        }

        private fun hideComments() {
            binding.commentsRecyclerView.animate().alpha(0f)
                .translationX(binding.commentsRecyclerView.width.toFloat()).setDuration(300)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator) {
                        super.onAnimationStart(animation)
                        binding.overlay.animate().alpha(0f).setDuration(300)
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        areCommentsShowing = false
                    }
                })
        }
    }
}