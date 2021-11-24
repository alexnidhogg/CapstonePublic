package sheridan.teodored.myapplication.chat

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
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sheridan.teodored.myapplication.R
import sheridan.teodored.myapplication.databinding.FragmentChatMenuBinding

class ChatMenuFragment : Fragment() {

    private var _binding: FragmentChatMenuBinding? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var listOfThreads: ArrayList<ChatMenuListElement>
    private lateinit var recyclerViewListAdapter: ChatMenuListAdapter
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
        _binding = FragmentChatMenuBinding.inflate(inflater, container, false)

        recyclerView = binding.ChatThreadRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.setHasFixedSize(true)

        listOfThreads = arrayListOf<ChatMenuListElement>()
        threads = arrayOf()

        auth = FirebaseAuth.getInstance()

        recyclerViewListAdapter = ChatMenuListAdapter(listOfThreads)
        recyclerView.adapter = recyclerViewListAdapter

        this.lifecycleScope.launch {
            withContext(Dispatchers.IO) {

                var tempList = arrayListOf<ChatMenuListElement>()
                var namesToLoad = mutableListOf<String>()
                var loadedNames = mutableMapOf<String, String>()
                //var loadedNames = arrayListOf<Pair<String, String>>()

                var query = Tasks.await(fireStore.collection("ChatThread").whereEqualTo("Sender", auth.currentUser?.uid).orderBy("LastUpdate", Query.Direction.DESCENDING).get(), 6, java.util.concurrent.TimeUnit.SECONDS)
                var second_query = Tasks.await(fireStore.collection("ChatThread").whereEqualTo("Recipient", auth.currentUser?.uid).orderBy("LastUpdate", Query.Direction.DESCENDING).get(), 6, java.util.concurrent.TimeUnit.SECONDS)

                //Chats you initiated
                for(i in 0..(query.count()-1)) {
                    val topic = ChatMenuListElement(query.documents[i].get("Recipient").toString(), query.documents[i].id, ::navigateToChatThread,  (query.documents[i].get("LastUpdate") as Timestamp).nanoseconds)
                    namesToLoad.add(topic.Title)
                    tempList.add(topic)
                }

                //Chats someone else initiated
                for(i in 0..(second_query.count()-1)) {
                    val topic = ChatMenuListElement(second_query.documents[i].get("Sender").toString(), second_query.documents[i].id, ::navigateToChatThread,  (second_query.documents[i].get("LastUpdate") as Timestamp).nanoseconds)
                    namesToLoad.add(topic.Title)
                    tempList.add(topic)
                }

                val tempRegList = tempList.sortedBy {
                    it.LastUpdate
                }.reversed()

                println(namesToLoad.count())
                println(namesToLoad[0])

                val loadedNamesQuery = Tasks.await(fireStore.collection("Users").whereIn("UserId", namesToLoad).get(), 6, java.util.concurrent.TimeUnit.SECONDS)

                println(loadedNamesQuery.count())

                for(i in 0..(loadedNamesQuery.count()-1)) {
                    println(loadedNamesQuery.documents[i].get("UserId").toString())
                    println(loadedNamesQuery.documents[i].get("FirstName").toString() + " " + loadedNamesQuery.documents[i].get("LastName").toString())
                    loadedNames.put(loadedNamesQuery.documents[i].get("UserId").toString(),
                        loadedNamesQuery.documents[i].get("FirstName")
                            .toString() + " " + loadedNamesQuery.documents[i].get("LastName")
                            .toString()
                    )
                }

                for(i in tempRegList) {
                    var Name = loadedNames.get(i.Title)
                    if (Name == null) {
                        Name = ""
                    }
                    listOfThreads.add(
                        ChatMenuListElement(Name, i.Id, i.callback, i.LastUpdate)
                    )
                }

                if (query.count() > 0 || second_query.count() > 0) {
                    recyclerViewListAdapter.notifyItemRangeInserted(0,query.count() + second_query.count())
                }
            }
        }

        binding.ChatBackButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.ChatLogOutButton.setOnClickListener {
            findNavController().navigateUp()
            findNavController().navigateUp()
            findNavController().navigateUp()
            auth.signOut()
        }

        return binding.root
    }

    fun navigateToChatThread(chat: String) : Void? {

        val bundle = bundleOf("Chat" to chat)
        findNavController().navigate(R.id.action_chatMenuFragment_to_chatMessageFragment, bundle)
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