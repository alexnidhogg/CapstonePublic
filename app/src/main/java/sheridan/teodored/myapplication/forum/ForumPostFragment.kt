package sheridan.teodored.myapplication.forum

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
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
                        var tempListOfPosts = mutableListOf<ForumPostListElement>()

                        for (doc in snapshot.documents) {
                            val post = ForumPostListElement(
                                doc.get("AuthorName").toString(),
                                doc.get("Author").toString(),
                                doc.get("Message").toString(),
                                (doc.get("TimePosted") as Timestamp).toDate(),
                                doc.id,
                                ::ShowContextMenu
                            )
                            if (!listOfPosts.contains(post)) {
                                tempListOfPosts.add(post)
                            }
                        }

                        tempListOfPosts = tempListOfPosts.sortedWith(compareBy{it.TimePosted}).toMutableList()

                        for(item in tempListOfPosts){
                             listOfPosts.add(item)
                        }

                        if (tempListOfPosts.size > 0) {
                            recyclerViewListAdapter.notifyItemRangeInserted(
                                listOfPosts.size - tempListOfPosts.size,
                                tempListOfPosts.size
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

    fun ShowContextMenu(Id : Int, Anchor : View, User : String) : Void? {
        val menu = PopupMenu(this.context, Anchor)
        menu.menu.apply {
            add("Report").setOnMenuItemClickListener {
                println("Reported")
                true
            }
            add("Send Message").setOnMenuItemClickListener {
                if(User == auth.currentUser?.uid){
                    //Do Nothing, Can't Message Yourself
                }
                else {
                    viewLifecycleOwner.lifecycleScope.launch {
                        withContext(Dispatchers.IO) {
                            val SenderCheck = Tasks.await(
                                fireStore.collection("ChatThread")
                                    .whereEqualTo("Recipient", auth.currentUser!!.uid)
                                    .whereEqualTo("Sender", User).get()
                            )
                            if (SenderCheck.count() == 0) {
                                val RecipientCheck = Tasks.await(
                                    fireStore.collection("ChatThread")
                                        .whereEqualTo("Sender", auth.currentUser!!.uid)
                                        .whereEqualTo("Recipient", User).get()
                                )
                                if (RecipientCheck.count() == 0) {
                                    // Create New Chat
                                    var data = mutableMapOf<String, Any>()
                                    data.put("Sender", auth.currentUser!!.uid)
                                    data.put("Recipient", User)
                                    data.put("LastUpdate", Timestamp.now())
                                    val CreateChat = Tasks.await(
                                        fireStore.collection("ChatThread").add(data)
                                    )
                                    findNavController().navigateUp()
                                    findNavController().navigateUp()
                                    findNavController().navigateUp()
                                    findNavController().navigate(R.id.action_SecondFragment_to_chatMenuFragment)
                                    val bundle = Bundle();
                                    bundle.putString("Chat", CreateChat.id)
                                    findNavController().navigate(
                                        R.id.action_chatMenuFragment_to_chatMessageFragment,
                                        bundle
                                    )
                                } else {
                                    //Go To Existing Chat
                                    findNavController().navigateUp()
                                    findNavController().navigateUp()
                                    findNavController().navigateUp()
                                    findNavController().navigate(R.id.action_SecondFragment_to_chatMenuFragment)
                                    val bundle = Bundle();
                                    bundle.putString("Chat", RecipientCheck.documents[0].id)
                                    findNavController().navigate(
                                        R.id.action_chatMenuFragment_to_chatMessageFragment,
                                        bundle
                                    )
                                }
                            } else {
                                //Go To Existing Chat
                                findNavController().navigateUp()
                                findNavController().navigateUp()
                                findNavController().navigateUp()
                                findNavController().navigate(R.id.action_SecondFragment_to_chatMenuFragment)
                                val bundle = Bundle();
                                bundle.putString("Chat", SenderCheck.documents[0].id)
                                findNavController().navigate(
                                    R.id.action_chatMenuFragment_to_chatMessageFragment,
                                    bundle
                                )
                            }
                        }
                    }
                }
                true
            }
        }
        menu.show()
        return null
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