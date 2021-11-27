package sheridan.teodored.myapplication.documentScan

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.text.Layout
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Tasks
import com.google.android.material.internal.NavigationMenuItemView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import sheridan.teodored.myapplication.R
import sheridan.teodored.myapplication.grade.GradesAdapter
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.Base64.Decoder
import kotlin.collections.ArrayList
import java.util.concurrent.TimeUnit


class DocumentScanAdapter (private val lifecycle: LifecycleCoroutineScope, private val images : ArrayList<DocumentScanSavedImage>, private val docId : ArrayList<String> ) : RecyclerView.Adapter<DocumentScanAdapter.DocumentsHolder>() {

    private var fireStore : FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth


    class DocumentsHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img : ImageView = itemView.findViewById(R.id.contentView)
        val save_pdf : Button = itemView.findViewById(R.id.download_pdf)
        val delete_pdf : Button = itemView.findViewById(R.id.delete_pdf)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentsHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.fragment_saved_image, parent,false)
        return DocumentsHolder(itemView)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: DocumentsHolder, position: Int) {

        val currentItem = images[position]
        val imageBytes = Base64.decode(currentItem.img64, 0)
        val img = BitmapFactory.decodeByteArray(imageBytes, 0 , imageBytes.size)

        holder.img.setImageBitmap(img)

        holder.save_pdf.setOnClickListener {
            val document: PdfDocument = PdfDocument()
            val pageInfo: PdfDocument.PageInfo = PdfDocument.PageInfo.Builder(img.width, img.height, 1).create()
            val page: PdfDocument.Page = document.startPage(pageInfo)

            val canvas: Canvas = page.canvas
            canvas.drawBitmap(img, 0f,0f,null)
            document.finishPage(page)

            val directoryPath: String = android.os.Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()


            document.writeTo( FileOutputStream(directoryPath + "/saved.pdf"))
            document.close()
            val msg = "PDF saved to downloads"
            Toast.makeText(holder.img.context, msg, Toast.LENGTH_LONG).show()
        }
        holder.delete_pdf.setOnClickListener {
           // val deletedDoc = fireStore.collection("UserDocs").whereEqualTo("docBase64", currentItem.img64).get()
            lifecycle.launch {
                withContext(Dispatchers.IO){
                    var query = Tasks.await(fireStore.collection("UserDocs").document(docId[position]).delete())
                    images.removeAt(position)
                    docId.removeAt(position)
                    this.launch {
                        withContext(Dispatchers.Main){
                            notifyItemRemoved(position)
                        }
                    }

                }
            }
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }


}