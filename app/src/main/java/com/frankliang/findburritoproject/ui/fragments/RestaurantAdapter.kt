package com.frankliang.findburritoproject.ui.fragments

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.frankliang.findburritoproject.R
import com.frankliang.findburritoproject.databinding.ItemRestaurantRowBinding
import com.frankliang.findburritoproject.model.Restaurant

class RestaurantAdapter(private val clickListener: OnRestaurantClickListener) :
    RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {
    private val diffCallBack = object : DiffUtil.ItemCallback<Restaurant>() {
        override fun areItemsTheSame(
            oldItem: Restaurant,
            newItem: Restaurant
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Restaurant,
            newItem: Restaurant
        ): Boolean {
            return oldItem == newItem
        }
    }
    private val mDiffer = AsyncListDiffer(this, diffCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val binding = DataBindingUtil.inflate<ItemRestaurantRowBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_restaurant_row,
            parent,
            false
        )
        return RestaurantViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val item = mDiffer.currentList[position]
        holder.onBind(item, position)
    }

    override fun getItemCount(): Int {
        return mDiffer.currentList.size
    }

    fun submitData(restaurants: List<Restaurant>) {
        val newList = mutableListOf<Restaurant>()
        newList.addAll(restaurants)
        mDiffer.submitList(newList)
    }

    class RestaurantViewHolder(private val binding: ItemRestaurantRowBinding,
                               private val clickListener: OnRestaurantClickListener) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: Restaurant, position: Int) {
            binding.restaurant = item
            val extra = initTransition(position)
            binding.container.setOnClickListener {
                clickListener.onRestaurantClick(item, extra, position)
            }
            binding.executePendingBindings()
        }

        private fun initTransition(position: Int): FragmentNavigator.Extras {
            val context = binding.tvAddress.context
            binding.tvAddress.transitionName = context.getString(R.string.trans_address, position)
            binding.tvInfo.transitionName = context.getString(R.string.trans_info, position)
            binding.container.transitionName = context.getString(R.string.trans_container, position)
            return FragmentNavigatorExtras(
                binding.tvAddress to context.getString(R.string.trans_address, position),
                binding.tvInfo to context.getString(R.string.trans_info, position),
                binding.container to context.getString(R.string.trans_container, position)
            )
        }
    }

    interface OnRestaurantClickListener {
        fun onRestaurantClick(restaurant: Restaurant, extras: FragmentNavigator.Extras, position: Int)
    }
}