package sheridan.teodored.myapplication

import DataObjects.Assignment
import Database.AppDatabase
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import sheridan.teodored.myapplication.databinding.FragmentAssignmentViewBinding
import sheridan.teodored.myapplication.forum.AssignmentAdapter
import sheridan.teodored.myapplication.forum.AssignmentData


class AssignmentViewFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    var _binding: FragmentAssignmentViewBinding? = null
    private val binding get() = _binding!!
    var arr = arrayListOf<AssignmentData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAssignmentViewBinding.inflate(inflater, container, false)
        recyclerView = binding.recy
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.setHasFixedSize(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val text = arguments?.getString("cam")
        val db = Room.databaseBuilder(
            requireContext().applicationContext,
            AppDatabase::class.java,
            "name"
        ).fallbackToDestructiveMigration().allowMainThreadQueries().build()
        val assignmentDb = db.assignmentDao()
        val stuff = assignmentDb.loadAllByName(text.toString())
        for (assignments in stuff){
            val assign = AssignmentData(assignments.assignmentName.toString(),assignments.assignmentGrade)
            arr.add(assign)
        }
        println(arr)
        var adapter = AssignmentAdapter(arr)
        recyclerView.adapter = adapter



    }

}