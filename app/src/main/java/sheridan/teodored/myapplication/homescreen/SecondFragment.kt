package sheridan.teodored.myapplication.homescreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import sheridan.teodored.myapplication.R
import sheridan.teodored.myapplication.databinding.FragmentSecondBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    private lateinit var auth: FirebaseAuth

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        if(auth.currentUser == null){
            findNavController().navigateUp()
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.GradesButton.setOnClickListener { findNavController().navigate(R.id.SecondFragment_to_Grades) }
        //log out
        binding.LogOut.setOnClickListener {
            auth.signOut()
            findNavController().navigateUp()
        }
        binding.UploadButton.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_DocumentScan)
        }

        binding.ForumButton.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_forumTopicFragment)
        }
        binding.SavedAssignmentsButton.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_saved_documents)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}