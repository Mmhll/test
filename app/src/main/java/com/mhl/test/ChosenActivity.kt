package com.mhl.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.*

class ChosenActivity : AppCompatActivity() {
    private lateinit var objectId : String
    val myRef : DatabaseReference = FirebaseDatabase.getInstance("https://test-c0aba-default-rtdb.europe-west1.firebasedatabase.app").getReference("EstateObjects")
    lateinit var estateObject : EstateObject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chosen)
        var bundle : Bundle? = intent.extras
        objectId = bundle?.getString("FirebaseID").toString()
        myRef.child(objectId).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                estateObject = snapshot.getValue(EstateObject::class.java)!!
                Toast.makeText(this@ChosenActivity, estateObject.floor.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }
}