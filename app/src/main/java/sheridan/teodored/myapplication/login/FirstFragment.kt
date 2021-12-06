package sheridan.teodored.myapplication.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import sheridan.teodored.myapplication.R
import sheridan.teodored.myapplication.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private lateinit var auth: FirebaseAuth
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
        //get instance of Firebase
        auth = FirebaseAuth.getInstance()

        //Check if there is a user currently logged in if true go to homepage
        if (FirebaseAuth.getInstance().currentUser == null){
            println("No User Found")
        }else{
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        binding.SignUp.setOnClickListener {
            findNavController().navigate(R.id.actionFirstFragment_to_SignUpFragment)
        }

        //Log user in using credentials
        binding.buttonFirst.setOnClickListener {
            val user = binding.UserName.text.toString()
            val pass = binding.UserPass.text.toString()
            println(user)
            auth.signInWithEmailAndPassword(user,pass).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
                }
            }.addOnFailureListener { exception ->
                println("Navigation Failed")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}