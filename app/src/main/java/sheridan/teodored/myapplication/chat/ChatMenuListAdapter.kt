package sheridan.teodored.myapplication.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import sheridan.teodored.myapplication.R

class ChatMenuListAdapter(private val topics : ArrayList<ChatMenuListElement>) : RecyclerView.Adapter<ChatMenuListAdapter.ChatMenuListHolder>() {

    class ChatMenuListHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        val button : Button = itemView.findViewById(R.id.forum_thread_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMenuListHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.forum_thread_list_item, parent, false)
        return ChatMenuListHolder(itemView)

    }

    override fun onBindViewHolder(holder: ChatMenuListHolder, position: Int) {

        val currentItem = topics[position]
        holder.button.setText(currentItem.Title)
        holder.button.setOnClickListener {
            currentItem.callback(currentItem.Id)
        }

    }

    override fun getItemCount(): Int {

        return topics.size;

    }

}