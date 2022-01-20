package com.mhl.test

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.awaitAll
import java.lang.Exception

class ObjectActivity : AppCompatActivity() {

    private lateinit var logoutButton: Button
    private lateinit var createButton: Button
    private lateinit var recyclerView: RecyclerView
    private var keyArrayList = arrayListOf<String>()
    val myRef : DatabaseReference = FirebaseDatabase.getInstance("https://test-c0aba-default-rtdb.europe-west1.firebasedatabase.app").getReference("EstateObjects")
    lateinit var arrayList : ArrayList<EstateObject>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_object)
        //Инициализация компонентов интерфейса
        logoutButton = findViewById(R.id.logout_button)
        createButton = findViewById(R.id.create_button)
        recyclerView = findViewById(R.id.object_recycle)


        recyclerView.layoutManager = LinearLayoutManager(this)

        arrayList = arrayListOf<EstateObject>()

        getEstateObjects()



        logoutButton.setOnClickListener {
            //Выход из учётной записи в Firebase
            Firebase.auth.signOut()
            //Переход на экран входа
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            //Завершение текущей активности
            finish()
        }

        createButton.setOnClickListener {
            //Переход на экран создания недвижимости
            val intent = Intent(this, CreateActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getEstateObjects() {
        myRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    arrayList.clear()
                    for (dataSnap in snapshot.children){
                        keyArrayList.add(dataSnap.key.toString())
                        val estateObject = dataSnap.getValue(EstateObject::class.java)
                        arrayList.add(estateObject!!)
                    }
                    var adapter = EstateAdapter(this@ObjectActivity, arrayList)
                    recyclerView.adapter = adapter
                    adapter.setOnItemClickListener(object : EstateAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            try {
                                var intent = Intent(this@ObjectActivity, ChosenActivity::class.java)
                                intent.putExtra("FirebaseID", keyArrayList[position])
                                startActivity(intent)
                            }
                            catch(e : Exception){
                                Log.d("Error", e.toString())
                            }
                        }

                    })

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}