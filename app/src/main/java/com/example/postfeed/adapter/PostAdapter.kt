package com.example.postfeed.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.postfeed.R
import com.example.postfeed.databinding.ItemPostBinding
import com.example.postfeed.databinding.ItemProgressBinding
import com.example.postfeed.model.Post
import com.example.postfeed.model.PostListData
import com.example.postfeed.utils.Utils

class PostAdapter(private val onLikeClick: (String, Int) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_POST = 0
        private const val VIEW_TYPE_LOADING = 1
    }

    val postList = mutableListOf<PostListData>()
    private var isLoading = false

    fun setLoading(loading: Boolean) {
        if (isLoading != loading) {
            isLoading = loading
            if (loading) {
                notifyItemInserted(postList.size)
            } else {
                notifyItemRemoved(postList.size)
            }
        }
    }

    fun submitList(data: List<PostListData>) {
        val oldList = postList.toList()
        postList.clear()
        postList.addAll(data)

        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = oldList.size
            override fun getNewListSize(): Int = postList.size
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition].post?._id == postList[newItemPosition].post?._id
            }
            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition] == postList[newItemPosition]
            }
        }
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int {
        return postList.size + if (isLoading) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoading && position == postList.size) VIEW_TYPE_LOADING else VIEW_TYPE_POST
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_POST) {
            val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            PostViewHolder(binding)
        } else {
            val binding = ItemProgressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            LoadingViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PostViewHolder) {
            val post = postList[position].post
            post?.let { holder.bind(it, onLikeClick, position) }
        }
    }

    class LoadingViewHolder(binding: ItemProgressBinding) :
        RecyclerView.ViewHolder(binding.root)

    class PostViewHolder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val mediaAdapter = MediaAdapter()

        init {
            binding.mediaViewPager.adapter = mediaAdapter
        }

        @SuppressLint("ClickableViewAccessibility")
        fun bind(post: Post, onLikeClick: (String, Int) -> Unit, position: Int) {

            binding.caption.visibility = View.VISIBLE
            binding.txtCount.visibility = View.GONE

            binding.mediaViewPager.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.txtCount.text = "${position + 1}/${mediaAdapter.itemCount}"
                }
            })

            // Bind data
            binding.txtUsername.text = post.userId.name
            binding.txtPostdate.text = Utils.getDate(post.createAt)
            binding.caption.text = post.description
            if (post.description.isEmpty()) binding.caption.visibility = View.GONE

            binding.txtLike.text = post.TotalLike.toString()
            binding.btnLike.setImageResource(
                if (post.selfLike) R.drawable.ic_like else R.drawable.ic_unlike
            )

            val profileUrl = "https://d3b13iucq1ptzy.cloudfront.net/${post.userId.profile}"
            Glide.with(binding.imgProfile.context)
                .load(profileUrl)
                .circleCrop()
                .placeholder(R.drawable.ic_placeholder)
                .into(binding.imgProfile)

            val mediaList = post.media.toMutableList()

            if (mediaList.size > 1) {
                binding.txtCount.text = "1/${mediaList.size}"
                binding.txtCount.visibility = View.VISIBLE
            }

            mediaAdapter.submitList(mediaList)

            binding.btnLike.setOnClickListener {
                onLikeClick(post._id, position)
                binding.btnLike.animate()
                    .scaleX(1.2f)
                    .scaleY(1.2f)
                    .setDuration(100)
                    .withEndAction {
                        binding.btnLike.animate()
                            .scaleX(1.0f)
                            .scaleY(1.0f)
                            .setDuration(100)
                            .start()
                    }
                    .start()
            }
        }
    }
}