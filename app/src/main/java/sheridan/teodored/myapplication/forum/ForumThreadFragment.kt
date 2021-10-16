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
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sheridan.teodored.myapplication.R
import sheridan.teodored.myapplication.databinding.FragmentForumThreadBinding

class ForumThreadFragment : Fragment() {

    private var _binding: FragmentForumThreadBinding? = null

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
        _binding = FragmentForumThreadBinding.inflate(inflater, container, false)

        topic = arguments?.getString("Topic")
        if(topic == null){
            findNavController().navigateUp()
            findNavController().navigateUp()
            return binding.root
        }

        recyclerView = binding.forumThreadRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.setHasFixedSize(true)

        listOfThreads = arrayListOf<ForumThreadListElement>()
        threads = arrayOf()

        recyclerViewListAdapter = ForumThreadListAdapter(listOfThreads)
        recyclerView.adapter = recyclerViewListAdapter

        binding.ForumThreadsTitle.text = topic

        this.lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                var query = Tasks.await(fireStore.collection("ForumThread").whereEqualTo("Topic", topic).orderBy("LastUpdate", Query.Direction.DESCENDING).get(), 6, java.util.concurrent.TimeUnit.SECONDS)

                for(i in 0..(query.count()-1)) {
                    val topic = ForumThreadListElement(query.documents[i].get("Title").toString(), query.documents[i].id, ::navigateToForumThread)
                    listOfThreads.add(topic)
                }
                if (query.count() > 0) {
                    recyclerViewListAdapter.notifyItemRangeInserted(0,query.count())
                }
            }
        }

        binding.forumThreadsBackButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.forumThreadsLogOutButton.setOnClickListener {
            findNavController().navigateUp()
            findNavController().navigateUp()
            findNavController().navigateUp()
            auth.signOut()
        }

        binding.forumThreadsNewThreadButton.setOnClickListener {
            val bundle = bundleOf("Topic" to topic)
            findNavController().navigate(R.id.action_forumThreadsFragment_to_forumNewThreadFragment, bundle)
        }

        return binding.root
    }

    fun navigateToForumThread(thread: String) : Void? {

        val bundle = bundleOf("Topic" to topic, "Thread" to thread)
        findNavController().navigate(R.id.action_forumThreadsFragment_to_forumPostFragment, bundle)
        return null

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        if (FirebaseAuth.getInstance().currentUser == null){
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