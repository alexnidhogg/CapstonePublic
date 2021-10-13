package sheridan.teodored.myapplication.forum

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import sheridan.teodored.myapplication.R

class ForumTopicAdapter(private val Topics : ArrayList<ForumTopicName>) : RecyclerView.Adapter<ForumTopicAdapter.ForumTopicHolder>() {

    class ForumTopicHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        val button : Button = itemView.findViewById(R.id.forum_topic_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForumTopicHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.forum_category_list_item, parent, false)
        return ForumTopicHolder(itemView)

    }

    override fun onBindViewHolder(holder: ForumTopicHolder, position: Int) {

        val currentItem = Topics[position]
        holder.button.setText(currentItem.Name)

    }

    override fun getItemCount(): Int {

        return Topics.size;

    }

}