package com.mhl.test

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.text.SimpleDateFormat
import java.util.*

class CreateActivity : AppCompatActivity() {
    //Объявление переменных
    private lateinit var addImageButton: Button
    private lateinit var addressText :EditText
    private lateinit var spaceText: EditText
    private lateinit var costText: EditText
    private lateinit var roomText: EditText
    private lateinit var floorText: EditText
    private lateinit var selectedImage : Uri
    private lateinit var addObjectButton: Button

    lateinit var now : Date
    //Инициализация констант
    val firebaseStorage = FirebaseStorage.getInstance().getReference("Images")
    val GALLERY_REQUEST = 1
    val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")
    val myRef = FirebaseDatabase.getInstance("https://test-c0aba-default-rtdb.europe-west1.firebasedatabase.app").getReference("/EstateObjects")

    var dateNow = ""
    var downloadUri : Uri = Uri.EMPTY



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        //Инициализация элементов интерфейса
        addressText = findViewById(R.id.address_input)
        spaceText = findViewById(R.id.space_input)
        costText = findViewById(R.id.cost_input)
        roomText = findViewById(R.id.rooms_input)
        floorText = findViewById(R.id.floor_input)
        addImageButton = findViewById(R.id.add_image_button)
        addObjectButton = findViewById(R.id.add_object_button)

        addImageButton.setOnClickListener {//Добавление слушателя для выбора изображения из галереи
            val intent = Intent(Intent.ACTION_PICK) //Параметр для intent, который даёт возможность выбрать какой-либо файл
            intent.type = "image/*"//Задание типа изображения для intent
            startActivityForResult(intent, GALLERY_REQUEST)//Использование intent для получения результата (изображения)
        }

        addObjectButton.setOnClickListener {
            //Проверка всех полей на пустоту
            if (emptyText(addressText) and emptyText(spaceText) and emptyText(costText) and emptyText(roomText) and emptyText(floorText) and !selectedImage.equals(null)){
                //Добавление изображения в FirebaseStorage и добавление пользователя в Realtime Database
                uploadImage(selectedImage)
                //Переход на главный экран
                intentToObject()
            }
            else{
                Toast.makeText(this, "Одно или несколько полей не заполнены", Toast.LENGTH_SHORT)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, imageReturnedIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent)

        when(requestCode){
            GALLERY_REQUEST ->
                if (resultCode == RESULT_OK){
                    selectedImage = imageReturnedIntent?.data!!//Инициализация Uri переменной, которая несёт в себе ссылку на файл2
                    Toast.makeText(this, "Файл выбран", Toast.LENGTH_SHORT).show()
                    addImageButton.setBackgroundColor(resources.getColor(R.color.green))//Смена цвета заднего фона кнопки для того, чтобы лучше было понятно, сделано ли это действие
                    now = Date()
                    dateNow = formatter.format(now)//Инициализирование переменной dateNow для указания названия файла в Firebase Storage
                }
        }
    }

    private fun uploadImage(uri: Uri){
        var ref : StorageReference = firebaseStorage.child(dateNow)
        var up : UploadTask = ref.putFile(uri)
        var task : Task<Uri> = up.continueWithTask {
            ref.downloadUrl
        }.addOnCompleteListener {
            downloadUri = it.result!!
            saveUser()
        }
    }

    private fun saveUser() {
        var estate = EstateObject(downloadUri.toString(),
            addressText.text.toString(),
            spaceText.text.toString().toDouble(),
            costText.text.toString().toDouble(),
            roomText.text.toString().toInt(),
            floorText.text.toString().toInt())
        //Добавление объекта estate в Firebase Realtime Database
        myRef.child(dateNow).setValue(estate)
    }

    private fun intentToObject(){
        val intent = Intent(this, ObjectActivity::class.java)
        startActivity(intent)
        finish()
    }


    private fun emptyText(text: EditText) : Boolean{
        if (text.text.toString().isEmpty()){
            text.error = "Поле не может быть пустым"
            return false
        }
        return true
    }
}