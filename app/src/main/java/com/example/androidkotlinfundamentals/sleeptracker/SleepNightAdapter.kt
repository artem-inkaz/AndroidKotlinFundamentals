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
import com.example.androidkotlinfundamentals.database.SleepNight

class SleepNightAdapter: RecyclerView.Adapter<TextItemViewHolder>(){
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolder {
       // create an instance of LayoutInflater.
       val layoutInflater = LayoutInflater.from(parent.context)
       // create the view by asking the layoutinflater to inflate it
       val view =layoutInflater
           .inflate(R.layout.text_item_view,parent,false) as TextView
       // return a TextItemViewHolder made with view
       return  TextItemViewHolder(view)
    }
    // function is called by RecyclerView to display the data for one list item
    // at the specified position. So the onBindViewHolder() method takes two arguments:
    // a view holder, and a position of the data to bind.
    // For this app, the holder is the TextItemViewHolder, and the position is the position
    // in the list.
    override fun onBindViewHolder(holder: TextItemViewHolder, position: Int) {
        val item = data[position]
        // This code displays only a list of numbers, but this simple example lets you see
        // how the adapter gets the data into the view holder and onto the screen.
        holder.textView.text = item.sleepQuality.toString()
        // set the text color to red in view holders that hold quality ratings that are less than or equal to 1 and represent poor sleep.
        if (item.sleepQuality <= 1) {
            holder.textView.setTextColor(Color.RED)
        } else {
            holder.textView.setTextColor(Color.BLACK)
        }
    }
    // o return the size of the list of sleep nights in data.
    // The RecyclerView needs to know how many items the adapter has for it to display,
    // and it does that by calling getItemCount()
    override fun getItemCount() = data.size
}

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    // get references to the views. You need a reference to the views that this ViewHolder will update.
    // Every time you bind this ViewHolder, you need to access the image and both text views.
// (You convert this code to use data binding later.)
    val sleepLength: TextView = itemView.findViewById(R.id.sleep_length)
    val quality: TextView = itemView.findViewById(R.id.quality_string)
    val qualityImage: ImageView = itemView.findViewById(R.id.quality_image)

}

