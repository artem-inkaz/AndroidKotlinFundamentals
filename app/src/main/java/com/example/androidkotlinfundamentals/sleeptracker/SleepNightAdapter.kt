package com.example.androidkotlinfundamentals.sleeptracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.androidkotlinfundamentals.database.SleepNight
import com.example.androidkotlinfundamentals.databinding.ListItemSleepNightBinding

class SleepNightAdapter(val clickListener: SleepNightListener): ListAdapter<SleepNight, SleepNightAdapter.ViewHolder>(SleepNightDiffCallBack()){
//        var data = listOf<SleepNight>()
   // To tell the RecyclerView when the data that it's displaying has changed,
   // add a custom setter to the data variable that's at the top of the SleepNightAdapter class.
   // In the setter, give data a new value, then call notifyDataSetChanged() to trigger redrawing
   // the list with the new data
//            set(value) {
//                field = value
   // When notifyDataSetChanged() is called, the RecyclerView redraws the whole list,
   // not just the changed items. This is simple, and it works for now. You improve on this code
   // later in this series of codelabs.
//                notifyDataSetChanged()
//            }
   // This function takes two parameters and returns a ViewHolder.
   // The parent parameter, which is the view group that holds the view holder,
   // is always the RecyclerView. The viewType parameter is used when there are multiple views
   // in the same RecyclerView. For example, if you put a list of text views, an image,
   // and a video all in the same RecyclerView, the onCreateViewHolder() function would need
   // to know what type of view to use.
   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder.from(parent)
   }

    // function is called by RecyclerView to display the data for one list item
    // at the specified position. So the onBindViewHolder() method takes two arguments:
    // a view holder, and a position of the data to bind.
    // For this app, the holder is the TextItemViewHolder, and the position is the position
    // in the list.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = data[position]
//        val item = getItem(position)
//        holder.bind(holder, item)
        holder.bind(getItem(position)!!,clickListener)
    }

    // Change the signature of the ViewHolder class so that the constructor is private.
    // Because from() is now a method that returns a new ViewHolder instance, there's no reason for
    // anyone to call the constructor of ViewHolder anymore.
    class ViewHolder private constructor(val binding: ListItemSleepNightBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: SleepNight, clickListener: SleepNightListener) {
            binding.sleep = item
            binding.clickListener = clickListener
            binding.executePendingBindings()

    }

    companion object {
        fun from(parent: ViewGroup): ViewHolder {
            // create an instance of LayoutInflater.
            val layoutInflater = LayoutInflater.from(parent.context)
            // Added binding
            // create the view by asking the layoutinflater to inflate it
//            val view = layoutInflater
//                .inflate(R.layout.list_item_sleep_night, parent, false)
            val binding = ListItemSleepNightBinding.inflate(layoutInflater,parent,false)
            // return a TextItemViewHolder made with view
            return ViewHolder(binding)
        }
      }
    }
}

class SleepNightDiffCallBack : DiffUtil.ItemCallback<SleepNight>(){
    override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        // To change body of created functions use File | Settings | File Templates.
        // If the items have the same nightId, they are the same item, so return true. Otherwise,
        // return false. DiffUtil uses this test to help discover if an item was added, removed, or moved.
        return  oldItem.nightId == newItem.nightId
    }

    override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        // To change body of created functions use File | Settings | File Templates.
        // Inside areContentsTheSame(), check whether oldItem and newItem contain the same data;
        // that is, whether they are equal. This equality check will check all the fields,
        // because SleepNight is a data class. Data classes automatically define equals and
        // a few other methods for you. If there are differences between oldItem and newItem,
        // this code tells DiffUtil that the item has been updated.
        return oldItem == newItem
    }
}

sealed class DataItem {
    // When the adapter uses DiffUtil to determine whether and how an item has changed,
    // the DiffItemCallback needs to know the id of each item. You will see an error,
    // because SleepNightItem and Header need to override the abstract property id.
    abstract val id: Long

    // The first is a SleepNightItem, which is a wrapper around a SleepNight,
    // so it takes a single value called sleepNight.
    // To make it part of the sealed class, have it extend DataItem
    data class SleepNightItem(val sleepNight: SleepNight): DataItem(){

        override val id = sleepNight.nightId
    }
    // The second class is Header, to represent a header. Since a header has no actual data,
    // you can declare it as an object. That means there will only ever be one instance of Header.
    // Again, have it extend DataItem
    object Header: DataItem(){
        override val id = Long.MIN_VALUE
    }
}

class SleepNightListener(val clickListener: (sleepId: Long) -> Unit){
    fun  onClick(night: SleepNight) = clickListener(night.nightId)
}