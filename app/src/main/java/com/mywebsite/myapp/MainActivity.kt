package com.mywebsite.myapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    //array list
    private val signInLauncher = registerForActivityResult( //создали объект авторизации экрана
        FirebaseAuthUIActivityResultContract()
    ) { resultCallback ->

        this.onSignInResult(resultCallback) //запуск самого экрана
    }

    private lateinit var database: DatabaseReference // создали объект для записи в БД

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        database = Firebase.database.reference //инициализация базы данных

        // Choose authentication providers
        Log.d("testLogs","111 RegistrationActivity start registration")
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build()) //список регистраций который мы используем

        // Create and launch sign-in intent

        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build() // создали интент для экрана firebase auth
        signInLauncher.launch(signInIntent) // запустили экран firebase auth
        Log.d("testLogs","111 Created finished")

    }


    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) { //0 -1 1
        val response = result.resultCode //результат с экрана Firebase auth
        if (result.resultCode == RESULT_OK) { //если результат ОК
            // Successfully signed in
            val authUser = FirebaseAuth.getInstance().currentUser //создаем объект текущего пользователя Firebase auth
            authUser?.let { // если он существует мы сохраняем его в базу данных
                val email = it.email.toString() // извлекаем email нашего пользователя
                val uid = it.uid // извлекаем uid нашего пользователя
                val firebaseUser = User(email, uid) // создаем новый объект User с параметрами email и uid
                Log.d("testLogs","RegistrationActivity firebaseUser $firebaseUser")

                database.child("users").child(uid).setValue(firebaseUser) // сохраняем нашего пользователя в Firebase Realtime

                val intentToAnotherScreen = Intent(this, MoviesActivity::class.java)
                startActivity(intentToAnotherScreen)
            }

            // ...
        } else if (result.resultCode == RESULT_CANCELED) { // если результат не ОК должны обработать ошибку
            Log.d("testLogs","RegistrationActivity registration failure")
            Toast.makeText(this@MainActivity, "Something wrong with registration", Toast.LENGTH_SHORT).show()
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        } else {
            //do not do anything
        }
    }
}