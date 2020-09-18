package com.example.underpressurea.pressuretracker

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.underpressurea.R

class PressureAdapter(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {

    var min_pressure:TextView
    var max_pressure:TextView
    var date: TextView
    var qualityImage:ImageView
    var btn_delete_item: ImageButton

    init{
        min_pressure = itemView.findViewById(R.id.min_pressure) as TextView
        max_pressure = itemView.findViewById(R.id.max_pressure) as TextView
        date = itemView.findViewById(R.id.date) as TextView
        qualityImage = itemView.findViewById(R.id.quality_image) as ImageView
        btn_delete_item = itemView.findViewById(R.id.btn_delete_item) as ImageButton

    }
}
/*class PressureAdapter: RecyclerView.Adapter<PressureAdapter.ViewHolder>() {

    var data =  listOf<PressureValue>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    class ViewHolder private constructor (itemView: View) : RecyclerView.ViewHolder(itemView){
        val min_pressure :TextView = itemView.findViewById(R.id.min_pressure)
        val max_pressure :TextView = itemView.findViewById(R.id.max_pressure)
        val date : TextView = itemView.findViewById(R.id.date)
        val levelImage :ImageView = itemView.findViewById(R.id.quality_image)

        fun bind(item: PressureValue) {
            val res = itemView.context.resources
            date.text =
                convertTimeToFormatted(item.startTimeMilli, res)
            levelImage.setImageResource(
                when (item.level) {
                    0 -> R.drawable.ic_pressure_bad
                    else -> R.drawable.ic_pressure_ok
                }
            )
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.list_item_pressure, parent, false)

                return ViewHolder(view)
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    /**var min_pressure:TextView
        var max_pressure:TextView
        init{
            min_pressure = itemView.findViewById(R.id.min_pressure) as TextView
            max_pressure = itemView.findViewById(R.id.max_pressure) as TextView
        }
        */

    /**
    var data =  listOf<PressureValue>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    override fun getItemCount() = data.size

    /** The onBindViewHolder()function is called by RecyclerView to
     * display the data for one list item at the specified position
    */
    override fun onBindViewHolder(holder: TextItemViewHolder, position: Int) {
        val item = data[position]
        holder.textView.text = item.level.toString()
    }

    /**onCreateViewHolder() is called when the RecyclerView needs a view holder to represent an item.*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val view = layoutInflater
            .inflate(R.layout.text_item_view, parent, false) as TextView
        return TextItemViewHolder(view)
    }
    */
}

 */
