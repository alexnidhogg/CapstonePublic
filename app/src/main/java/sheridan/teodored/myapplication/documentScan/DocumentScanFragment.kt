package sheridan.teodored.myapplication.documentScan

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import sheridan.teodored.myapplication.databinding.FragmentDocumentScanBinding


class DocumentScanFragment : Fragment() {

    private var _binding: FragmentDocumentScanBinding? = null

    private val binding get() = _binding!!

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
            binding.preview.setImageBitmap(img)
            binding.preview.isVisible = true
        }else{
            println("Nah G")
            super.onActivityResult(requestCode, resultCode, data)
        }

    }


}