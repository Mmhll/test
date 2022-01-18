package com.mhl.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern


class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var emailText : EditText
    private lateinit var passwordText : EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth //Инициализирование аутентификации

        //Инициализация компонентов интерфейса
        emailText = findViewById(R.id.mail_input)
        passwordText = findViewById(R.id.password_input)
        loginButton = findViewById(R.id.login_button)

        var emailString = emailText.text.toString()
        var passwordString = passwordText.text.toString()

        loginButton.setOnClickListener {
            //Проверка Email и пароля на валидность
            if (checkEmail(emailText.text.toString()) and  checkPassword(passwordText.text.toString())){
                //Firebase вход через email и password, полученные из полей emailText и passwordText
                signIn(emailText.text.toString(), passwordText.text.toString())
            }
            else if (!checkEmail(emailText.text.toString())){
                emailText.error = "Вы ввели почту неверно"
            }
            else if (!checkPassword(passwordText.text.toString())){
                passwordText.error = "Вы ввели пароль неверно"
            }
        }



    }
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null){
            //Если пользователь авторизован, идёт переход на окно с объектами
            intentToMain()
        }
    }
    private fun signIn(email: String, password: String) {
        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Если все данные введены верно и пользователь существует, выполняется переход на главное окно
                    intentToMain()
                } else {
                    // Если вход провалился, то пользователь получит это сообщение в виде всплывающего окна
                    Toast.makeText(this, "Такого пользователя не существует или вы ввели неправильный логин/пароль, попробуйте снова", Toast.LENGTH_SHORT).show()
                }
            }
        // [END sign_in_with_email]
    }


    private fun intentToMain(){
        val intent : Intent = Intent(this, ObjectActivity::class.java)
        startActivity(intent)
        finish()
    }


    private fun checkEmail(mail : String): Boolean {
        var pattern = Pattern.compile("[a-zA-Z0-9\\_\\-]{3,32}" +
                "\\@" +
                "[a-zA-Z]{2,12}" +
                "\\." +
                "[a-zA-Z]{2,6}")
        //Проверка email при помощи regex
        return pattern.matcher(mail).matches()
    }


    private fun checkPassword(password : String) : Boolean {
        var pattern = Pattern.compile("[a-zA-Z0-9]{6,30}")
        return pattern.matcher(password).matches()
    }
}