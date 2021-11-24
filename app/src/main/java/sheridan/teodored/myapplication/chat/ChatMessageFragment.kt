package sheridan.teodored.myapplication.chat

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sheridan.teodored.myapplication.databinding.FragmentChatMessageBinding
import kotlin.collections.ArrayList

class ChatMessageFragment : Fragment() {

    private var _binding: FragmentChatMessageBinding? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var listOfPosts: ArrayList<ChatMessageListElement>
    private lateinit var recyclerViewListAdapter: ChatMessageListAdapter
    private lateinit var posts : Array<String>

    private var loaded = false;

    private var chat : String? = null

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
        _binding = FragmentChatMessageBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()

        chat = arguments?.getString("Chat")
        if(chat == null) {
            findNavController().navigateUp()
            findNavController().navigateUp()
            findNavController().navigateUp()
            return binding.root
        }

        recyclerView = binding.ChatMessagesRecyclerview
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.setHasFixedSize(true)

        listOfPosts = arrayListOf<ChatMessageListElement>()
        posts = arrayOf()

        recyclerViewListAdapter = ChatMessageListAdapter(listOfPosts)
        recyclerView.adapter = recyclerViewListAdapter

        println(chat)

        val docRef = fireStore.collection("ChatThread").document(chat!!).collection("Messages")

        this.lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                listener = docRef.addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        //error
                    }
                    println(snapshot)
                    if (snapshot != null) {
                        var tempListOfPosts = mutableListOf<ChatMessageListElement>()

                        for (doc in snapshot.documents) {
                            val post = ChatMessageListElement(
                                doc.get("AuthorName").toString(),
                                doc.get("Text").toString(),
                                (doc.get("TimePosted") as Timestamp).toDate(),
                                doc.id
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
                            loaded = true
                        }
                    }
                }

                var result = Tasks.await(fireStore.collection("Users").whereEqualTo("UserId", auth.currentUser!!.uid).get())
                MyName = result.documents[0].get("FirstName").toString() + " " + result.documents[0].get("LastName").toString()

                var second_result = Tasks.await(fireStore.collection("ChatThread").document(chat!!).get())
                if(second_result.get("Sender") == auth.currentUser!!.uid) {
                    val Name = Tasks.await(fireStore.collection("Users").whereEqualTo("UserId", second_result.get("Recipient").toString()).get()
                    )
                    this.launch {
                        withContext(Dispatchers.Main) {
                            binding.ChatMessagesTitle.text = Name.documents[0].get("FirstName").toString() + " " + Name.documents[0].get("LastName").toString()
                        }
                    }
                }
                else
                {
                    val Name = Tasks.await(fireStore.collection("Users").whereEqualTo("UserId", second_result.get("Sender").toString()).get())
                    this.launch {
                        withContext(Dispatchers.Main){
                            binding.ChatMessagesTitle.text = Name.documents[0].get("FirstName").toString() + " " + Name.documents[0].get("LastName").toString()
                        }
                    }
                }
            }
        }

        binding.ChatMessagesBackButton.setOnClickListener {
            findNavController().navigateUp()
            if(listOfPosts.count() == 0 && loaded){
                fireStore.collection("ChatThread").document(chat!!).delete()
            }
        }

        binding.ChatMessagesLogOutButton.setOnClickListener {
            findNavController().navigateUp()
            findNavController().navigateUp()
            findNavController().navigateUp()
            auth.signOut()
        }

        binding.ChatMessagesSendButton.setOnClickListener {
            if(binding.ChatMessagesTextField.text.isNotEmpty()){
                var data = mutableMapOf<String, Any>()
                data.put("Author", auth.currentUser!!.uid)
                data.put("AuthorName", MyName)
                data.put("Text", binding.ChatMessagesTextField.text.toString())
                data.put("TimePosted", Timestamp.now())
                fireStore.collection("ChatThread").document(chat!!).collection("Messages").add(data)
                binding.ChatMessagesTextField.text.clear()
                fireStore.collection("ChatThread").document(chat!!).update("LastUpdate", Timestamp.now())
            }
        }

        binding.ChatMessagesTextField.requestFocus()

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