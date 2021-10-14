package sheridan.teodored.myapplication

import DataObjects.Class
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room.databaseBuilder
import sheridan.teodored.myapplication.databinding.FragmentGradesBinding
import sheridan.teodored.myapplication.forum.ForumTopicAdapter
import Database.AppDatabase as AppDatabase
import sheridan.teodored.myapplication.forum.ForumTopicName
import sheridan.teodored.myapplication.forum.GradesAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Grades.newInstance] factory method to
 * create an instance of this fragment.
 */
class Grades : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private var _binding: FragmentGradesBinding? = null
    private val binding get() = _binding!!
    val arr = arrayListOf<ForumTopicName>()

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
        // Inflate the layout for this fragment
        val testClass = Class(uid = 1, className = "test")
        val stuff = classD.getAll()
        val text = EditText(requireContext())
        for (classes in stuff){
            val topic = ForumTopicName(classes.className.toString())
            arr.add(topic)
        }
        println(arr)
        var adapter = GradesAdapter(arr)
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener(object : GradesAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                println(arr[position].Name)


            }
        })


        binding.addClass.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext()).setTitle("Im gay")
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


