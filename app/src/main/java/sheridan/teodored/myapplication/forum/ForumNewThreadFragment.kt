package sheridan.teodored.myapplication.forum

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope

import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sheridan.teodored.myapplication.R
import sheridan.teodored.myapplication.databinding.FragmentForumNewThreadBinding

class ForumNewThreadFragment : Fragment() {

    private var _binding: FragmentForumNewThreadBinding? = null

    private var fireStore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private var topic : String? = null

    private var MyName : String = ""

    private lateinit var auth: FirebaseAuth
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentForumNewThreadBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()

        topic = arguments?.get("Topic").toString()
        if(topic == null) {
            findNavController().navigateUp()
            findNavController().navigateUp()
            findNavController().navigateUp()
            return binding.root
        }

        binding.forumNewThreadBackButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.forumNewThreadLogOutButton.setOnClickListener {
            auth.signOut()
            findNavController().navigateUp()
            findNavController().navigateUp()
            findNavController().navigateUp()
            findNavController().navigateUp()
        }

        var working = false;

        binding.forumNewThreadCreateButton.setOnClickListener {
            println(MyName)
            if(!working) {
                if (binding.forumNewThreadNameEditText.text.isNotEmpty() && binding.forumNewThreadMessageEditText.text.isNotEmpty()) {
                    working = true
                    this.lifecycleScope.launch {
                        withContext(Dispatchers.IO) {
                            var data = mutableMapOf<String, Any>()
                            data.put("Author", auth.currentUser!!.uid)
                            data.put("LastUpdate", Timestamp.now())
                            data.put("Title", binding.forumNewThreadNameEditText.text.toString())
                            data.put("Topic", topic!!)
                            var query = Tasks.await(fireStore.collection("ForumThread").add(data))
                            var post = mutableMapOf<String, Any>()
                            post.put("Author", auth.currentUser!!.uid)
                            post.put("AuthorName", MyName)
                            post.put("Message", binding.forumNewThreadMessageEditText.text.toString())
                            post.put("TimePosted", Timestamp.now())
                            var second_query = Tasks.await(fireStore.collection("ForumThread").document(query.id).collection("Posts").add(post))

                            val bundle = bundleOf("Topic" to topic, "Thread" to query.id)
                            findNavController().navigateUp()
                            findNavController().navigate(R.id.action_forumThreadsFragment_to_forumPostFragment, bundle)
                        }
                    }
                }
            }
        }

        this.lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                var result = Tasks.await(fireStore.collection("Users").whereEqualTo("UserId", auth.currentUser!!.uid).get())
                println(result.documents)
                MyName = result.documents[0].get("FirstName").toString() + " " + result.documents[0].get("LastName").toString()
                println(MyName)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (auth.currentUser == null){
            findNavController().navigateUp()
            findNavController().navigateUp()
            findNavController().navigateUp()
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
