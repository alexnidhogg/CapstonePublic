package sheridan.teodored.myapplication.forum

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import sheridan.teodored.myapplication.R

class GradesAdapter (private val Topics : ArrayList<ForumTopicName>) : RecyclerView.Adapter<GradesAdapter.GradesHolder>() {

    private lateinit var mList : onItemClickListener
    interface onItemClickListener{
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(listener: onItemClickListener){
        mList = listener
    }

    class GradesHolder (itemView : View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){
        val button : Button = itemView.findViewById(R.id.forum_topic_button)

        init{
            button.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GradesHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.forum_category_list_item, parent, false)
        return GradesHolder(itemView,mList)

    }

    override fun onBindViewHolder(holder: GradesHolder, position: Int) {

        val currentItem = Topics[position]
        holder.button.setText(currentItem.Name)
        holder.button.setOnClickListener {
            mList.onItemClick(position)
        }

    }

    override fun getItemCount(): Int {

        return Topics.size;

    }



}