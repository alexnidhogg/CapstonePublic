package sheridan.teodored.myapplication.grade

import Database.AppDatabase
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import sheridan.teodored.myapplication.R
import sheridan.teodored.myapplication.databinding.FragmentAssignmentViewBinding


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

    @SuppressLint("WrongConstant")
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

        binding.buttonAddAssignment.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext())
            val Layout = LinearLayout(requireContext())
            val input = EditText(requireContext())
            val input1 = EditText(requireContext())
            input.setHint("Assignment Name")
            input1.setHint("Assignment Grade")
            Layout.setOrientation(1)
            Layout.addView(input)
            Layout.addView(input1)
            dialog.setView(Layout)
            dialog.setTitle("New Assignment")
            dialog.setPositiveButton(
                "Add", DialogInterface.OnClickListener { dialogInterface, i
                    ->
                    val testObj = AssignmentData(input.text.toString(),(input1.text.toString()).toInt())
                    arr.add(testObj)
                    adapter.notifyDataSetChanged()
                    assignmentDb.insertAll(DataObjects.Assignment(uid = 0, className = text.toString(), assignmentName = testObj.Name, assignmentGrade = testObj.Grade))

                })



            dialog.show()


        }



    }

}