package sheridan.teodored.myapplication.forum

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import sheridan.teodored.myapplication.R

class AssignmentAdapter(private val assignments : ArrayList<AssignmentData>) : RecyclerView.Adapter<AssignmentAdapter.AssignmentDataHolder>() {

    class AssignmentDataHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        val assignmentName : TextView = itemView.findViewById(R.id.name)
        val assignmentGrade : TextView = itemView.findViewById(R.id.grade)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssignmentDataHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_assignment_list_item, parent, false)
        return AssignmentDataHolder(itemView)
    }

    override fun onBindViewHolder(holder: AssignmentDataHolder, position: Int) {
        val currentItem = assignments[position]
        holder.assignmentName.setText(currentItem.Name)
        holder.assignmentGrade.setText("Grade: " + currentItem.Grade.toString())
    }

    override fun getItemCount(): Int {
        return assignments.size
    }
}