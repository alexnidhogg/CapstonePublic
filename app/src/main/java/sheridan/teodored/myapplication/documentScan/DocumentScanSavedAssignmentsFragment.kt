package sheridan.teodored.myapplication.documentScan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sheridan.teodored.myapplication.databinding.FragmentDocumentScanSavedAssignmentsBinding


class DocumentScanSavedAssignmentsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    var _binding : FragmentDocumentScanSavedAssignmentsBinding? = null
    private val binding get() = _binding!!
    private var fireStore : FirebaseFirestore = FirebaseFirestore.getInstance()
    private var topic : String? = null




    var arr = arrayListOf<DocumentScanSavedImage>()
    var docIdArray = arrayListOf<String>()
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDocumentScanSavedAssignmentsBinding.inflate(inflater, container, false)


        recyclerView = binding.ViewImages
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.setHasFixedSize(true)
        auth = FirebaseAuth.getInstance()




        var recyclerViewAdapter = DocumentScanAdapter(this.lifecycleScope, arr, docIdArray)
        recyclerView.adapter = recyclerViewAdapter
        // Inflate the layout for this fragment
        this.lifecycleScope.launch {
            println(FirebaseAuth.getInstance().currentUser?.uid)
            withContext(Dispatchers.IO){
                var query = Tasks.await(fireStore.collection("UserDocs").whereEqualTo("user", FirebaseAuth.getInstance().currentUser?.uid).get(),6,java.util.concurrent.TimeUnit.SECONDS)
                    for (i in 0..(query.count() - 1)) {

                        val byteString = query.documents[i].get("docBase64").toString()
                        val assignment = DocumentScanSavedImage(
                            img64 = byteString
                        )
                        arr.add(assignment)
                        docIdArray.add(query.documents[i].id)
                    }
                    if (query.count() > 0) {
                        recyclerViewAdapter.notifyItemRangeInserted(0, query.count())
                    }
            }
        }


        return binding.root
    }


}