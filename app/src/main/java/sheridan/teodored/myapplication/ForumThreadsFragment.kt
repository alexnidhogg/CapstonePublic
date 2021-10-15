package sheridan.teodored.myapplication

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import sheridan.teodored.myapplication.databinding.FragmentForumThreadsBinding
import sheridan.teodored.myapplication.databinding.FragmentForumTopicBinding
import sheridan.teodored.myapplication.forum.ForumThreadListAdapter
import sheridan.teodored.myapplication.forum.ForumThreadListElement
import sheridan.teodored.myapplication.forum.ForumTopicListAdapter
import sheridan.teodored.myapplication.forum.ForumTopicListElement

class ForumThreadsFragment : Fragment() {

    private var _binding: FragmentForumThreadsBinding? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var listOfThreads: ArrayList<ForumThreadListElement>
    private lateinit var recyclerViewListAdapter: ForumThreadListAdapter
    private lateinit var threads : Array<String>

    private var topic : String? = null

    private var fireStore : FirebaseFirestore = FirebaseFirestore.getInstance()

    private lateinit var auth: FirebaseAuth
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentForumThreadsBinding.inflate(inflater, container, false)

        topic = arguments?.getString("Topic")

        println(topic)

        recyclerView = binding.forumThreadRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.setHasFixedSize(true)

        listOfThreads = arrayListOf<ForumThreadListElement>()
        threads = arrayOf()

        recyclerViewListAdapter = ForumThreadListAdapter(listOfThreads)
        recyclerView.adapter = recyclerViewListAdapter

        this.lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                var query = Tasks.await(fireStore.collection("ForumThread").whereEqualTo("Topic", topic).get(), 6, java.util.concurrent.TimeUnit.SECONDS)

                for(i in 0..(query.count()-1)) {
                    val topic = ForumThreadListElement(query.documents[i].get("Title").toString(), query.documents[i].id)
                    listOfThreads.add(topic)
                }
                if (query.count() > 0) {
                    recyclerViewListAdapter.notifyItemRangeInserted(0,query.count())
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        if (FirebaseAuth.getInstance().currentUser == null){
            findNavController().navigate(R.id.action_forumThreadsFragment_to_FirstFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}