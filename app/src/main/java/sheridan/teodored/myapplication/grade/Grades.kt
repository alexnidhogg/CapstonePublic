package sheridan.teodored.myapplication.grade

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room.databaseBuilder
import sheridan.teodored.myapplication.R
import sheridan.teodored.myapplication.databinding.FragmentGradesBinding
import sheridan.teodored.myapplication.forum.ForumThreadListElement
import sheridan.teodored.myapplication.forum.ForumTopicListElement
import Database.AppDatabase as AppDatabase

class Grades : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private var _binding: FragmentGradesBinding? = null
    private val binding get() = _binding!!
    val arr = arrayListOf<AssignmentClassListElement>()

    var cl : String = "test"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGradesBinding.inflate(inflater, container, false)
        recyclerView = binding.recy
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.setHasFixedSize(true)


        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = databaseBuilder(requireContext().applicationContext, AppDatabase::class.java, "name").fallbackToDestructiveMigration().allowMainThreadQueries().build()
        val classD = db.classDao()
        val stuff = classD.getAll()
        val text = EditText(requireContext())
        for (classes in stuff){
            val topic = AssignmentClassListElement(classes.className.toString())
            arr.add(topic)
        }
        println(arr)
        var adapter = GradesAdapter(arr)
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener(object : GradesAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                println(arr[position].Name)
                val bundle = bundleOf("cam" to arr[position].Name)
                findNavController().navigate(R.id.action_grades_to_assignments, bundle)



            }
        })


        binding.addClass.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext()).setTitle("New Class")
                .setMessage("Add a new class?")
                .setView(text)
                .setPositiveButton(
                    "Add", DialogInterface.OnClickListener { dialogInterface, i
                        -> classD.insertAll(DataObjects.Class(uid = 0, className = text.text.toString())) })
                .setNegativeButton("Cancel", null)
                .create()
            dialog.show()
        }


    }
}


