package com.ifs21039.lostfound.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ifs21039.lostfound.data.remote.response.LostFoundsItemsResponse
import com.ifs21039.lostfound.databinding.ItemRowLostfoundBinding

class ItemsAdapter :
    ListAdapter<LostFoundsItemsResponse,
            ItemsAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback
    private var originalData = mutableListOf<LostFoundsItemsResponse>()
    private var filteredData = mutableListOf<LostFoundsItemsResponse>()
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowLostfoundBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = originalData[originalData.indexOf(getItem(position))]

        holder.binding.cbItemLostfoundIsFinished.setOnCheckedChangeListener(null)
        holder.binding.cbItemLostfoundIsFinished.setOnLongClickListener(null)
        holder.bind(data)
        holder.binding.cbItemLostfoundIsFinished.setOnCheckedChangeListener { _, isChecked ->
            data.isFinished = if (isChecked) 1 else 0
            holder.bind(data)
            onItemClickCallback.onCheckedChangeListener(data, isChecked)
        }
        holder.binding.ivItemLostfoundDetail.setOnClickListener {
            onItemClickCallback.onClickDetailListener(data.id)
        }
    }
    class MyViewHolder(val binding: ItemRowLostfoundBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: LostFoundsItemsResponse) {
            binding.apply {
                tvItemLostfoundTitle.text = data.title
                cbItemLostfoundIsFinished.isChecked = data.isFinished == 1
            }
        }
    }
    fun submitOriginalList(list: List<LostFoundsItemsResponse>) {
        originalData = list.toMutableList()
        filteredData = list.toMutableList()
        submitList(originalData)
    }
    fun filter(query: String) {
        filteredData = if (query.isEmpty()) {
            originalData
        } else {
            originalData.filter {
                (it.title.contains(query, ignoreCase = true))
            }.toMutableList()
        }
        submitList(filteredData)
    }
    interface OnItemClickCallback {
        fun onCheckedChangeListener(todo: LostFoundsItemsResponse, isChecked: Boolean)
        fun onClickDetailListener(todoId: Int)
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<LostFoundsItemsResponse>() {
            override fun areItemsTheSame(
                oldItem: LostFoundsItemsResponse,
                newItem: LostFoundsItemsResponse
            ): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(
                oldItem: LostFoundsItemsResponse,
                newItem: LostFoundsItemsResponse
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
