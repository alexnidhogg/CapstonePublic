package sheridan.teodored.myapplication.SignUp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import sheridan.teodored.myapplication.R
import sheridan.teodored.myapplication.databinding.FragmentSignUpBinding


class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private lateinit var auth: FirebaseAuth
    private val binding get() = _binding!!
    private var fireStore : FirebaseFirestore = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        binding.SignUpAction.setOnClickListener {
            val userFName = binding.NewUserFName.text.toString()
            var userLName = binding.NewUserLName.text.toString()
            var email = binding.NewUser.text.toString()
            var password = binding.password.text.toString()
            if (binding.password.text.toString() == binding.passwordConfirm.text.toString()){
                var newUserObject: UserObject = UserObject(userFName, userLName, email, password)
                auth.createUserWithEmailAndPassword(newUserObject.Email, newUserObject.Password).addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        var newUserUid = auth.currentUser?.uid
                        var data : MutableMap<String, String> = mutableMapOf()
                        data.put("FirstName", newUserObject.FName)
                        data.put("LastName", newUserObject.LName)
                        if (newUserUid != null) {
                            data.put("UserId", newUserUid)
                        }
                        fireStore.collection("Users").add(data.toMap())
                        findNavController().navigate(R.id.action_SignUpFragment_to_HomeScreen)

                    }
                }
            }

        }



    }


}