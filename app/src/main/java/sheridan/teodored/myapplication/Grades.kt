package sheridan.teodored.myapplication

import DataObjects.Class
import Database.ClassDao
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import sheridan.teodored.myapplication.databinding.FragmentFirstBinding
import sheridan.teodored.myapplication.databinding.FragmentGradesBinding
import Database.AppDatabase as AppDatabase

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
    private var _binding: FragmentGradesBinding? = null
    private val binding get() = _binding!!
    var cl : String = "test"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGradesBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = databaseBuilder(requireContext().applicationContext, AppDatabase::class.java, "name").fallbackToDestructiveMigration().allowMainThreadQueries().build()
        val classD = db.classDao()
        // Inflate the layout for this fragment
        val testClass = Class(uid = 1, className = "test")
        val stuff = classD.getAll()
        println(stuff)
        val text = EditText(requireContext())

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