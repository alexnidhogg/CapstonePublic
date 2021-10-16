package sheridan.teodored.myapplication.forum

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
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.type.Date
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sheridan.teodored.myapplication.R
import sheridan.teodored.myapplication.databinding.FragmentForumPostBinding
import kotlin.collections.ArrayList

class ForumPostFragment : Fragment() {

    private var _binding: FragmentForumPostBinding? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var listOfPosts: ArrayList<ForumPostListElement>
    private lateinit var recyclerViewListAdapter: ForumPostListAdapter
    private lateinit var posts : Array<String>

    private var thread : String? = null
    private var topic : String? = null

    private var MyName : String = ""

    private var fireStore : FirebaseFirestore = FirebaseFirestore.getInstance()

    private lateinit var auth: FirebaseAuth
    private val binding get() = _binding!!

    private var authors = mutableMapOf<String, String>()

    private lateinit var listener : ListenerRegistration

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentForumPostBinding.inflate(inflater, container, false)

        thread = arguments?.getString("Thread")
        topic = arguments?.getString("Topic")
        if(thread == null || topic == null) {
            findNavController().navigateUp()
            findNavController().navigateUp()
            findNavController().navigateUp()
            return binding.root
        }

        auth = FirebaseAuth.getInstance()

        recyclerView = binding.forumPostRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.setHasFixedSize(true)

        listOfPosts = arrayListOf<ForumPostListElement>()
        posts = arrayOf()

        recyclerViewListAdapter = ForumPostListAdapter(listOfPosts)
        recyclerView.adapter = recyclerViewListAdapter

        val docRef = fireStore.collection("ForumThread").document(thread!!).collection("Posts")

        this.lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                listener = docRef.addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        //error
                    }
                    println(snapshot)
                    if (snapshot != null) {
                        val oldLength = listOfPosts.size
                        var numberToAdd = 0
                        for (doc in snapshot.documents) {
                            val post = ForumPostListElement(
                                doc.get("AuthorName").toString(),
                                doc.get("Message").toString(),
                                (doc.get("TimePosted") as Timestamp).toDate(),
                                doc.id
                            )
                            if (!listOfPosts.contains(post)) {
                                listOfPosts.add(post)
                                numberToAdd += 1
                            }
                        }
                        if (numberToAdd > 0) {
                            recyclerViewListAdapter.notifyItemRangeInserted(
                                oldLength,
                                numberToAdd
                            )
                        }
                    }
                }

                var result = Tasks.await(fireStore.collection("Users").whereEqualTo("UserId", auth.currentUser!!.uid).get())
                MyName = result.documents[0].get("FirstName").toString() + " " + result.documents[0].get("LastName").toString()

                var second_result = Tasks.await(fireStore.collection("ForumThread").document(thread!!).get())
                this.launch {
                    withContext(Dispatchers.Main){
                        binding.ForumPostTitle.text = second_result.get("Title").toString()
                    }
                }
            }
        }

        binding.forumPostBackButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.forumLogOutButton.setOnClickListener {
            findNavController().navigateUp()
            findNavController().navigateUp()
            findNavController().navigateUp()
            findNavController().navigateUp()
            auth.signOut()
        }

        binding.forumPostButton.setOnClickListener {
            if(binding.forumPostText.text.isNotEmpty()){
                var data = mutableMapOf<String, Any>()
                data.put("Author", auth.currentUser!!.uid)
                data.put("AuthorName", MyName)
                data.put("Message", binding.forumPostText.text.toString())
                data.put("TimePosted", Timestamp.now())
                fireStore.collection("ForumThread").document(thread!!).collection("Posts").add(data)
                binding.forumPostText.text.clear()
                fireStore.collection("ForumThread").document(thread!!).update("LastUpdate", Timestamp.now())
            }
        }

        binding.forumPostText.requestFocus()

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
        listener.remove()

    }
}