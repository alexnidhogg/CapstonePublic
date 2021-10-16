package sheridan.teodored.myapplication.forum

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sheridan.teodored.myapplication.R
import sheridan.teodored.myapplication.databinding.FragmentForumTopicBinding

class ForumTopicFragment : Fragment() {

    private var _binding: FragmentForumTopicBinding? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var listOfTopics: ArrayList<ForumTopicListElement>
    private lateinit var recyclerViewListAdapter: ForumTopicListAdapter
    private lateinit var topics : Array<String>

    private var fireStore : FirebaseFirestore = FirebaseFirestore.getInstance()

    private lateinit var auth: FirebaseAuth
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentForumTopicBinding.inflate(inflater, container, false)

        recyclerView = binding.forumTopicsRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.setHasFixedSize(true)

        listOfTopics = arrayListOf()
        topics = arrayOf()
        for (it in topics) {
            val topic = ForumTopicListElement(it, ::navigateToForumTopic)
            listOfTopics.add(topic)
        }
        recyclerViewListAdapter = ForumTopicListAdapter(listOfTopics)
        recyclerView.adapter = recyclerViewListAdapter

        this.lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                var query = Tasks.await(fireStore.collection("ForumTopic").get(), 2, java.util.concurrent.TimeUnit.SECONDS)

                for(i in 0..(query.count()-1)) {
                    val topic = ForumTopicListElement(query.documents[i].get("Name").toString(), ::navigateToForumTopic)
                    listOfTopics.add(topic)
                }

                recyclerViewListAdapter.notifyItemRangeInserted(0,query.count())
            }
        }

        binding.forumTopicsBackButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.forumTopicsLogOutButton.setOnClickListener {
            auth.signOut()
            findNavController().navigateUp()
            findNavController().navigateUp()
        }

        return binding.root
    }

    fun navigateToForumTopic(name: String) : Void? {

        val bundle = bundleOf("Topic" to name)
        findNavController().navigate(R.id.action_forumTopicFragment_to_forumThreadsFragment, bundle)
        return null

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        if (FirebaseAuth.getInstance().currentUser == null){
            findNavController().navigateUp()
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}