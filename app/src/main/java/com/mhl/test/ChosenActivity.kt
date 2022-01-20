package com.mhl.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class ChosenActivity : AppCompatActivity() {

    private lateinit var objectId : String
    private lateinit var changeButton: Button
    private lateinit var chosenImage : ImageView
    private lateinit var addressText: TextView
    private lateinit var spaceText : TextView
    private lateinit var roomsText : TextView
    private lateinit var floorText : TextView
    private lateinit var costText : TextView
    private lateinit var mCostText : TextView

    //Добавление databaseReference, где хранятся данные
    val myRef : DatabaseReference = FirebaseDatabase
        .getInstance("https://test-c0aba-default-rtdb.europe-west1.firebasedatabase.app")
        .getReference("EstateObjects")
    var estateObject : EstateObject = EstateObject()//Создание экземпляра класса EstateObject, общего для всей Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chosen)

        chosenImage = findViewById(R.id.image_chosen)
        changeButton = findViewById(R.id.button_change)
        addressText = findViewById(R.id.address_chosen)
        spaceText = findViewById(R.id.space_chosen)
        roomsText = findViewById(R.id.rooms_chosen)
        floorText = findViewById(R.id.floor_chosen)
        costText = findViewById(R.id.cost_chosen)
        mCostText = findViewById(R.id.m_cost_chosen)

        var bundle : Bundle? = intent.extras//Получение экстра данных из activity, из которой был совершён переход в текущую

        //Получение экстра строки FirebaseID из bundle
        objectId = bundle?.getString("FirebaseID").toString()
        //Получение данных объекта, id которого = objectId
        myRef.child(objectId).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                estateObject = snapshot.getValue(EstateObject::class.java)!!
                //Вывод полученных данных в GUI
                putData(estateObject)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        //Переход в activity для изменения параметров
        changeButton.setOnClickListener{
            var intent = Intent(this, ChangeActivity::class.java)
            intent.putExtra("FirebaseID", objectId)
            startActivity(intent)
        }

    }

    fun putData(estateObject: EstateObject){
        Picasso.with(this).load(estateObject.photo?.toUri()).into(chosenImage)

        addressText.text = "Адрес: " + (estateObject.address)
        spaceText.text = String.format("Площадь: %.1fм",estateObject.space)
        roomsText.text = "Количество комнат: " + (estateObject.rooms.toString())
        floorText.text = "Этаж: " + (estateObject.floor.toString())
        costText.text = String.format("Цена: %.2fр",estateObject.cost)
        mCostText.text = String.format("Цена за м²: %.2fр",(estateObject.cost?.div(estateObject.space!!)))
    }
}