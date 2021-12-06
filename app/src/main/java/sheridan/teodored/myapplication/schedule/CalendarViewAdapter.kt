package sheridan.teodored.myapplication.schedule

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import sheridan.teodored.myapplication.R

class CalendarViewAdapter : RecyclerView.Adapter<CalendarViewAdapter.CalendarTileHolder> {

    private var currentDate : Int = 0;
    private var currentMonth : Int = 0;
    private var currentYear : Int = 0;

    private var initialDate : Int = 0;
    private var initialMonth : Int = 0;
    private var initialYear : Int = 0;

    private var callback : ((Int) -> Unit)? = null;

    private var statuses : ArrayList<Status> = arrayListOf<Status>()

    class CalendarTileHolder : RecyclerView.ViewHolder {

        var self : View;
        var text : TextView;
        var current : ImageView;
        var overdue : ImageView;
        var future : ImageView;
        var done : ImageView;
        var selected : ImageView;

        constructor(itemView : View) : super(itemView) {
            self = itemView;
            text = itemView.findViewById(R.id.Calendar_Tile_Text)
            current = itemView.findViewById(R.id.Calendar_Current_Day_Image)
            overdue = itemView.findViewById(R.id.Calendar_Alert_Image)
            future = itemView.findViewById(R.id.Calendar_Schedule_Image)
            done = itemView.findViewById(R.id.Calendar_Done_Image)
            selected = itemView.findViewById(R.id.Calendar_Tile_Selected_Image)
        }
    }

    fun UpdateSelected(position: Int){

        if(mData[position] != ""){
            for (i in statuses) i.Selected = false
            statuses[position].Selected = true

            notifyItemRangeChanged(0,mData.count())
            if(callback != null){
                callback!!(position)
            }
        }
    }

    private var mData : ArrayList<String>;
    private var mInflater : LayoutInflater;

    constructor(context : Context, data : ArrayList<String>){
        mInflater = LayoutInflater.from(context)
        mData = data;
    }

    fun setInitialDate(date : Int, month : Int, year : Int, callback : (Int) -> Unit) {
        initialYear = year
        initialMonth = month
        initialDate = date
        this.callback = callback
    }

    fun updateStatuses(statuses : ArrayList<Status>){
        while (this.statuses.isNotEmpty()) this.statuses.removeLast()
        for (i in statuses) this.statuses.add(i)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarTileHolder {

        return CalendarTileHolder(mInflater.inflate(R.layout.calendar_tile, parent, false))

    }

    override fun onBindViewHolder(holder: CalendarTileHolder, position: Int) {
        holder.text.text = mData[position]
        holder.self.setOnClickListener {
            UpdateSelected(position)
        }
        holder.future.visibility =
            if (statuses[position].Schedule) View.VISIBLE
            else View.INVISIBLE
        holder.current.visibility =
            if (statuses[position].Current) View.VISIBLE
            else View.INVISIBLE
        holder.overdue.visibility =
            if (statuses[position].Warning) View.VISIBLE
            else View.INVISIBLE
        holder.done.visibility =
            if (statuses[position].Done) View.VISIBLE
            else View.INVISIBLE
        holder.selected.visibility =
            if (statuses[position].Selected) View.VISIBLE
            else View.INVISIBLE
    }

    override fun getItemCount(): Int {
        return mData.count();
    }

}