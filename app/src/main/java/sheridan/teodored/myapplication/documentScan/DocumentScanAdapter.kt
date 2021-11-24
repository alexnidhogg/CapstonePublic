package sheridan.teodored.myapplication.documentScan

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.text.Layout
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.internal.NavigationMenuItemView
import sheridan.teodored.myapplication.R
import sheridan.teodored.myapplication.grade.GradesAdapter
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.Base64.Decoder
import kotlin.collections.ArrayList

class DocumentScanAdapter (private val images : ArrayList<DocumentScanSavedImage>) : RecyclerView.Adapter<DocumentScanAdapter.DocumentsHolder>() {

    class DocumentsHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img : ImageView = itemView.findViewById(R.id.contentView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentsHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.fragment_saved_image, parent,false)
        return DocumentsHolder(itemView)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: DocumentsHolder, position: Int) {
        val currentItem = images[position]
        println(currentItem.img64)
        val imageBytes = Base64.decode(currentItem.img64, 0)
        val img = BitmapFactory.decodeByteArray(imageBytes, 0 , imageBytes.size)

        holder.img.setImageBitmap(img)

    }

    override fun getItemCount(): Int {
        return images.size
    }


}