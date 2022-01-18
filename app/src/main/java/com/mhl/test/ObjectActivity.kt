package com.mhl.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.awaitAll

class ObjectActivity : AppCompatActivity() {

    private lateinit var logoutButton: Button
    private lateinit var createButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_object)
        //Инициализация компонентов интерфейса
        logoutButton = findViewById(R.id.logout_button)
        createButton = findViewById(R.id.create_button)

        logoutButton.setOnClickListener {
            //Выход из учётной записи в Firebase
            Firebase.auth.signOut()
            //Переход на экран входа
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            //Завершение текущей активности
            finish()
        }
    }
}