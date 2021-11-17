package sheridan.teodored.myapplication.documentScan2

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_document_photo.*
import sheridan.teodored.myapplication.R
import sheridan.teodored.myapplication.databinding.FragmentDocumentPhotoBinding
import sheridan.teodored.myapplication.databinding.FragmentDocumentScanBinding
import sheridan.teodored.myapplication.documentScan.DocumentData
import java.io.ByteArrayOutputStream
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class documentPhotoFragment : Fragment() {
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService

    private var _binding: FragmentDocumentPhotoBinding? = null

    private val binding get() = _binding!!

    private var fireStore : FirebaseFirestore = FirebaseFirestore.getInstance()


    private lateinit var auth: FirebaseAuth




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (allPermissionsGranted()) {
            startPhoto()
        } else {
            ActivityCompat.requestPermissions(requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
        outputDirectory = getOutputDirectory()

        cameraExecutor = Executors.newSingleThreadExecutor()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDocumentPhotoBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.takephoto.setOnClickListener { takePhoto() }


    }
    private fun takePhoto(){
        val imageCapture = imageCapture ?:return

        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis()) + ".jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    binding.PreviewImage.visibility = View.VISIBLE
                    binding.viewFinder.visibility = View.GONE
                    binding.PreviewImage.setImageURI(savedUri)

                    // set the saved uri to the image view
                    val bmp: Bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, savedUri)
                    auth = FirebaseAuth.getInstance()

                    val upload = DocumentData(FirebaseAuth.getInstance().currentUser?.uid, encode(bmp))
                    var data : MutableMap<String, String> = mutableMapOf()
                    data.put("user", upload.userId.toString())
                    data.put("docBase64", upload.imgBase.toString())
                    fireStore.collection("UserDocs").add(data.toMap())
                    val msg = "Photo capture succeeded: $savedUri"
                    Toast.makeText(requireContext().applicationContext, msg, Toast.LENGTH_LONG).show()
                    Log.d(TAG, msg)
                }
            })

    }

    private fun startPhoto(){
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener(
            Runnable {
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder()
                    .build().also { it.setSurfaceProvider(binding.viewFinder.createSurfaceProvider()) }


                imageCapture = ImageCapture.Builder().build()

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    cameraProvider.unbindAll()

                    cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
                } catch (exc: Exception) {
                    println("Fuck")
                }

            },
            ContextCompat.getMainExecutor(this.requireContext()))
    }
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            // If all permissions granted , then start Camera
            if (allPermissionsGranted()) {
                startPhoto()
            } else {
                // If permissions are not granted,
                // present a toast to notify the user that
                // the permissions were not granted.\
            }
        }
    }
    private fun getOutputDirectory(): File {
        val mediaDir = requireContext().externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else requireContext().filesDir
    }
    private fun encode(bm: Bitmap) : String?{
        val byte = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.PNG,100,byte)
        val b = byte.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    companion object {
        private const val TAG = "CameraXGFG"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 20
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}
