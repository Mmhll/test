package com.mhl.test

import android.content.Intent
import android.graphics.Bitmap
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.getBitmap
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import java.io.IOError
import java.io.IOException
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.component1
import com.google.firebase.storage.ktx.component2
import com.google.firebase.storage.ktx.component3
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import com.mhl.test.databinding.ActivityCreateBinding
import java.text.SimpleDateFormat
import java.util.*

class CreateActivity : AppCompatActivity() {
    private lateinit var addImageButton: Button
    private lateinit var imageView: ImageView
    val GALLERY_REQUEST = 1
    val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")
    private lateinit var selectedImage : Uri
    lateinit var now : Date

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        addImageButton = findViewById(R.id.add_image_button)

        addImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, GALLERY_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, imageReturnedIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent)

        when(requestCode){
            GALLERY_REQUEST ->
                if (resultCode == RESULT_OK){
                    selectedImage = imageReturnedIntent?.data!!
                    now = Date()
                    val fileName = formatter.format(now)
                    Toast.makeText(this, "Файл выбран", Toast.LENGTH_SHORT).show()
                    addImageButton.setBackgroundColor(resources.getColor(R.color.green))
                    var firebaseStorage = FirebaseStorage.getInstance().getReference("images/$fileName")
                    firebaseStorage.putFile(selectedImage)
                }
        }

    }

}