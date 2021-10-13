package sheridan.teodored.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import sheridan.teodored.myapplication.databinding.FragmentForumTopicBinding
import sheridan.teodored.myapplication.forum.ForumTopicAdapter
import sheridan.teodored.myapplication.forum.ForumTopicName

class ForumTopicFragment : Fragment() {

    private var _binding: FragmentForumTopicBinding? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var listOfTopics: ArrayList<ForumTopicName>
    private lateinit var topics : Array<String>

    private lateinit var auth: FirebaseAuth
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentForumTopicBinding.inflate(inflater, container, false)

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.setHasFixedSize(true)

        listOfTopics = arrayListOf()
        topics = arrayOf("Mathematics", "Science", "Languages", "Social Science", "Arts")
        for (it in topics) {
            val topic = ForumTopicName(it)
            listOfTopics.add(topic)
        }
        recyclerView.adapter = ForumTopicAdapter(listOfTopics)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        if (FirebaseAuth.getInstance().currentUser == null){
            findNavController().navigate(R.id.action_forumTopicFragment_to_FirstFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}