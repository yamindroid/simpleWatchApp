package com.ymo.ui.component.now_playing.adapter

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ymo.databinding.ItemCommentBinding
import com.ymo.data.model.api.TopComment
import com.ymo.utils.loadFromUrl

class CommentAdapter : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {
    private var comments: List<TopComment> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder =
        CommentViewHolder(
            ItemCommentBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )


    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) =
        holder.bind(comments[position])

    override fun getItemCount(): Int = comments.size

    fun submitList(newComments: List<TopComment>) {
        comments = newComments
        notifyDataSetChanged()
    }

    class CommentViewHolder(private val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(topComment: TopComment) {
            binding.apply {
                commentText.apply {
                    text = topComment.comment
                    typeface = Typeface.create(
                        Typeface.createFromAsset(context.assets, "fonts/robot-flex-regular.ttf"),
                        Typeface.BOLD
                    )
                }
                avatarImage.loadFromUrl(topComment.avatar)
                executePendingBindings()
            }
        }
    }
}