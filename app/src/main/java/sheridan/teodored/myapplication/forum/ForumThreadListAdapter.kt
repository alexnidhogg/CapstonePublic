package sheridan.teodored.myapplication.forum

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import sheridan.teodored.myapplication.R

class ForumThreadListAdapter(private val topics : ArrayList<ForumThreadListElement>) : RecyclerView.Adapter<ForumThreadListAdapter.ForumThreadListHolder>() {

    class ForumThreadListHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        val button : Button = itemView.findViewById(R.id.forum_thread_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForumThreadListHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.forum_thread_list_item, parent, false)
        return ForumThreadListHolder(itemView)

    }

    override fun onBindViewHolder(holder: ForumThreadListHolder, position: Int) {

        val currentItem = topics[position]
        holder.button.setText(currentItem.Title)

    }

    override fun getItemCount(): Int {

        return topics.size;

    }

}