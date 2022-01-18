package com.mhl.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var emailText : EditText
    private lateinit var passwordText : EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Инициализация компонентов интерфейса
        emailText = findViewById(R.id.mail_input)
        passwordText = findViewById(R.id.password_input)
        loginButton = findViewById(R.id.login_button)
        loginButton.setOnClickListener {

        }

        auth = Firebase.auth //Инициализирование аутентификации

    }
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null){
            val intent : Intent = Intent(this, ObjectActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}