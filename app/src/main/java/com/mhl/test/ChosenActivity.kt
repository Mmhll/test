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

    val myRef : DatabaseReference = FirebaseDatabase.getInstance("https://test-c0aba-default-rtdb.europe-west1.firebasedatabase.app").getReference("EstateObjects")
    lateinit var estateObject : EstateObject

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

        var bundle : Bundle? = intent.extras
        objectId = bundle?.getString("FirebaseID").toString()
        Log.d("TAG", objectId)
        myRef.child(objectId).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                estateObject = snapshot.getValue(EstateObject::class.java)!!
                putData(estateObject)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


        changeButton.setOnClickListener{
            var intent = Intent(this, ChangeActivity::class.java)
            intent.putExtra("FirebaseID", objectId)
            startActivity(intent)
        }

    }
    fun putData(estateObject: EstateObject){
        Picasso.with(this).load(estateObject.photo?.toUri()).into(chosenImage)
        addressText.text = "Адрес: " + (estateObject.address)
        spaceText.text = "Площадь: " + estateObject.space.toString()
        roomsText.text = "Количество комнат: " + (estateObject.rooms.toString())
        floorText.text = "Этаж: " + (estateObject.floor.toString())
        costText.text = "Цена: " + (estateObject.cost.toString())
        mCostText.text = "Цена за метр²:" + ((estateObject.cost?.div(estateObject.space!!)).toString())
    }
}