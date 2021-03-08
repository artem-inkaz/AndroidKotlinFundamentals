package com.example.androidkotlinfundamentals.sleeptracker

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidkotlinfundamentals.R
import com.example.androidkotlinfundamentals.TextItemViewHolder
import com.example.androidkotlinfundamentals.convertDurationToFormatted
import com.example.androidkotlinfundamentals.convertNumericQualityToString
import com.example.androidkotlinfundamentals.database.SleepNight

class SleepNightAdapter: RecyclerView.Adapter<SleepNightAdapter.ViewHolder>(){
        var data = listOf<SleepNight>()
   // To tell the RecyclerView when the data that it's displaying has changed,
   // add a custom setter to the data variable that's at the top of the SleepNightAdapter class.
   // In the setter, give data a new value, then call notifyDataSetChanged() to trigger redrawing
   // the list with the new data
            set(value) {
                field = value
   // When notifyDataSetChanged() is called, the RecyclerView redraws the whole list,
   // not just the changed items. This is simple, and it works for now. You improve on this code
   // later in this series of codelabs.
                notifyDataSetChanged()
            }
   // This function takes two parameters and returns a ViewHolder.
   // The parent parameter, which is the view group that holds the view holder,
   // is always the RecyclerView. The viewType parameter is used when there are multiple views
   // in the same RecyclerView. For example, if you put a list of text views, an image,
   // and a video all in the same RecyclerView, the onCreateViewHolder() function would need
   // to know what type of view to use.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       // create an instance of LayoutInflater.
       val layoutInflater = LayoutInflater.from(parent.context)
       // create the view by asking the layoutinflater to inflate it
       val view =layoutInflater
           .inflate(R.layout.list_item_sleep_night,parent,false)
       // return a TextItemViewHolder made with view
       return  ViewHolder(view)
    }
    // function is called by RecyclerView to display the data for one list item
    // at the specified position. So the onBindViewHolder() method takes two arguments:
    // a view holder, and a position of the data to bind.
    // For this app, the holder is the TextItemViewHolder, and the position is the position
    // in the list.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        val res = holder.itemView.context.resources
        holder.sleepLength.text = convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli,res)
        holder.quality.text = convertNumericQualityToString(item.sleepQuality, res)

        holder .qualityImage.setImageResource(when (item.sleepQuality){
            0 -> R.drawable.ic_sleep_0
            1 -> R.drawable.ic_sleep_1
            2 -> R.drawable.ic_sleep_2
            3 -> R.drawable.ic_sleep_3
            4 -> R.drawable.ic_sleep_4
            5 -> R.drawable.ic_sleep_5
            else -> R.drawable.ic_sleep_active
        })
    }
    // o return the size of the list of sleep nights in data.
    // The RecyclerView needs to know how many items the adapter has for it to display,
    // and it does that by calling getItemCount()
    override fun getItemCount() = data.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        // get references to the views. You need a reference to the views that this ViewHolder will update.
        // Every time you bind this ViewHolder, you need to access the image and both text views.
// (You convert this code to use data binding later.)
        val sleepLength: TextView = itemView.findViewById(R.id.sleep_length)
        val quality: TextView = itemView.findViewById(R.id.quality_string)
        val qualityImage: ImageView = itemView.findViewById(R.id.quality_image)

    }
}



