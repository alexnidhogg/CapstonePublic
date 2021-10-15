package sheridan.teodored.myapplication.forum

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import sheridan.teodored.myapplication.R

class ForumTopicListAdapter(private val topics : ArrayList<ForumTopicListElement>) : RecyclerView.Adapter<ForumTopicListAdapter.ForumTopicHolder>() {

    class ForumTopicHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        val button : Button = itemView.findViewById(R.id.forum_topic_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForumTopicHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.forum_topic_list_item, parent, false)
        return ForumTopicHolder(itemView)

    }

    override fun onBindViewHolder(holder: ForumTopicHolder, position: Int) {

        val currentItem = topics[position]
        holder.button.setText(currentItem.Name)
        holder.button.setOnClickListener {
            currentItem.Callback(currentItem.Name)
        }

    }

    override fun getItemCount(): Int {

        return topics.size;

    }

}