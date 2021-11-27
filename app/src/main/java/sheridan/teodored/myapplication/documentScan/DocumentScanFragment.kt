package sheridan.teodored.myapplication.documentScan

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import sheridan.teodored.myapplication.databinding.FragmentDocumentScanBinding
import java.io.ByteArrayOutputStream

///DEPRECATED
class DocumentScanFragment : Fragment() {

    private var _binding: FragmentDocumentScanBinding? = null

    private val binding get() = _binding!!

    private var fireStore : FirebaseFirestore = FirebaseFirestore.getInstance()


    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentDocumentScanBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.openCam.setOnClickListener {
            val takePicInt = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            startActivityForResult(takePicInt, 43)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 43 && resultCode == Activity.RESULT_OK){
            val img = data?.extras?.get("data") as Bitmap
            auth = FirebaseAuth.getInstance()
            val upload = DocumentData(FirebaseAuth.getInstance().currentUser?.uid, encode(img))
            binding.preview.setImageBitmap(img)
            binding.preview.isVisible = true
            var writeData : MutableMap<String, String> = mutableMapOf()
            writeData.put("user", upload.userId.toString())
            writeData.put("docBase64", upload.imgBase.toString())
            fireStore.collection("UserDocs").add(writeData.toMap())
        }else{
            println("Nah G")
            super.onActivityResult(requestCode, resultCode, data)
        }

    }

    private fun encode(bm: Bitmap) : String?{
        val byte = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.PNG,100,byte)
        val b = byte.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }


}