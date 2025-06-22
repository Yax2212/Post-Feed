package com.example.postfeed.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.postfeed.R
import com.example.postfeed.databinding.ItemMediaBinding
import com.example.postfeed.model.Media

class MediaAdapter : RecyclerView.Adapter<MediaAdapter.MediaViewHolder>() {

    private val mediaList = mutableListOf<Media>()

    fun submitList(newList: List<Media>) {
        mediaList.clear()
        mediaList.addAll(newList.filter { it.type == "Image" })
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val binding = ItemMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MediaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        holder.bind(mediaList[position])
    }

    override fun getItemCount(): Int = mediaList.size

    inner class MediaViewHolder(private val binding: ItemMediaBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(media: Media) {

            val imageUrl = "https://d3b13iucq1ptzy.cloudfront.net/uploads/posts/image/${media.url}"
            Glide.with(binding.mediaImage.context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_placeholder)
                .into(binding.mediaImage)
        }

    }


}