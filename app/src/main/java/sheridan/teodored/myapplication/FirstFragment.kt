package sheridan.teodored.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import sheridan.teodored.myapplication.databinding.FragmentFirstBinding
import java.util.jar.Attributes

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private lateinit var auth: FirebaseAuth
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        if (FirebaseAuth.getInstance().currentUser == null){
            println("Isnt logged")
        }else{
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        binding.buttonFirst.setOnClickListener {
            val user = binding.UserName.text.toString()
            val pass = binding.UserPass.text.toString()
            println(user)
            auth.signInWithEmailAndPassword(user,pass).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
                }
            }.addOnFailureListener { exception ->
                println("nice cock")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun Login(view: View){
        val user = binding.UserName.text.toString()
        val pass = _binding?.UserPass?.text.toString()
        println(user)
        auth.signInWithEmailAndPassword(user,pass).addOnCompleteListener { task ->
            if(task.isSuccessful){
                findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            }
        }.addOnFailureListener { exception ->
            println("nice cock")
        }
    }


}