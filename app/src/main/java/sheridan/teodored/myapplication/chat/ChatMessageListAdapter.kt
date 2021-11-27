package sheridan.teodored.myapplication.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import sheridan.teodored.myapplication.R

class ChatMessageListAdapter(private val topics : ArrayList<ChatMessageListElement>) : RecyclerView.Adapter<ChatMessageListAdapter.ChatMessageListHolder>() {

    class ChatMessageListHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        val AuthorLabel : TextView = itemView.findViewById(R.id.Author_Label)
        val MessageLabel : TextView = itemView.findViewById(R.id.Message_Label)
        val TimeStampLabel : TextView = itemView.findViewById(R.id.TimeStamp_Label)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessageListHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.forum_post_list_item, parent, false)
        return ChatMessageListHolder(itemView)

    }

    override fun onBindViewHolder(holder: ChatMessageListHolder, position: Int) {

        val currentItem = topics[position]
        holder.AuthorLabel.text = currentItem.Author
        holder.MessageLabel.text = currentItem.Message
        holder.TimeStampLabel.text = currentItem.TimePosted.toString()

    }

    override fun getItemCount(): Int {

        return topics.size;

    }

}