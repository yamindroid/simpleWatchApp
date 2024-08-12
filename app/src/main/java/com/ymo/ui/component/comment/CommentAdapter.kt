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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]
        holder.bind(comment)
    }

    override fun getItemCount(): Int = comments.size

    fun submitList(newComments: List<TopComment>) {
        comments = newComments
        notifyDataSetChanged()
    }

    class CommentViewHolder(private val binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: TopComment) {
            binding.comment = comment
            binding.commentText.apply {
                typeface =Typeface.create(
                    Typeface.createFromAsset(context.assets, "fonts/robot-flex-regular.ttf"),
                    Typeface.BOLD
                )
            }
            binding.avatarImage.loadFromUrl(comment.avatar)
            binding.executePendingBindings()
        }
    }
}
