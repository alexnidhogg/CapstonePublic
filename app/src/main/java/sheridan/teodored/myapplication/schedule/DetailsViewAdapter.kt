package sheridan.teodored.myapplication.schedule

import DataObjects.TimeChunk
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sheridan.teodored.myapplication.R

class DetailsViewAdapter : RecyclerView.Adapter<DetailsViewAdapter.DetailElement> {

    private var mData : ArrayList<TimeChunk>;
    private var mInflater : LayoutInflater;

    class DetailElement : RecyclerView.ViewHolder {

        constructor(itemView : View) : super(itemView){

        }
    }

    constructor(context : Context, data : ArrayList<TimeChunk>){
        mInflater = LayoutInflater.from(context)
        mData = data;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailElement {

        return DetailElement(mInflater.inflate(R.layout.calendar_tile, parent, false))

    }

    override fun onBindViewHolder(holder: DetailElement, position: Int) {



    }

    override fun getItemCount(): Int {

        return mData.count()

    }


}