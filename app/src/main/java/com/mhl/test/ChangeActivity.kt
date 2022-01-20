package com.mhl.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.*
import java.lang.NumberFormatException
import kotlin.math.floor

class ChangeActivity : AppCompatActivity() {

    private lateinit var objectId : String
    private lateinit var saveButton: Button
    private lateinit var spaceInput : EditText
    private lateinit var costInput : EditText
    private lateinit var roomsInput : EditText
    private lateinit var floorInput : EditText
    //Добавление databaseReference, где хранятся данные
    val myRef : DatabaseReference = FirebaseDatabase
        .getInstance("https://test-c0aba-default-rtdb.europe-west1.firebasedatabase.app")
        .getReference("EstateObjects")
    var estateObject : EstateObject = EstateObject()//Создание экземпляра класса EstateObject, общего для всей Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change)
        var bundle : Bundle? = intent.extras//Получение экстра данных из activity, из которой был совершён переход в текущую

        saveButton = findViewById(R.id.save_button)
        spaceInput = findViewById(R.id.change_space)
        costInput = findViewById(R.id.change_cost)
        roomsInput = findViewById(R.id.change_rooms)
        floorInput = findViewById(R.id.change_floor)
        //Получение экстра строки FirebaseID из bundle
        objectId = bundle?.getString("FirebaseID").toString()
        //Получение данных объекта, id которого = objectId
        myRef.child(objectId).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                estateObject = snapshot.getValue(EstateObject::class.java)!!
                //Передача начальных данных объекта в GUI
                dataToUI(estateObject)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        //Сохранение изменённых данных в Realtime Database
        saveButton.setOnClickListener{
                estateObject.space = spaceInput.text.toString().toDouble()
                estateObject.cost = costInput.text.toString().toDouble()
                estateObject.rooms = roomsInput.text.toString().toInt()
                estateObject.floor = floorInput.text.toString().toInt()

                myRef.child(objectId).setValue(estateObject).addOnCompleteListener {
                    finish()
                }
        }

    }
    fun dataToUI(estate: EstateObject){
        spaceInput.setText(estate.space.toString())
        costInput.setText(estate.cost.toString())
        roomsInput.setText(estate.rooms.toString())
        floorInput.setText(estate.floor.toString())
    }
}