package com.epsi.shopshoes

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // On initialise Firebase Auth
        auth = Firebase.auth

        // On récupère les éléments de la vue dans des variables pour pouvoir les manipuler
        val etEmail: EditText = findViewById(R.id.email)
        val etPassword: EditText = findViewById(R.id.password)
        val btnConnect = findViewById<Button>(R.id.connect)

        btnConnect.setOnClickListener {
            Log.d("btntouch", "Le bouton à bien été cliqué")
            signIn(
                etEmail.text.toString(),
                etPassword.text.toString(),
            )
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }
    }

    private fun signIn(email: String, password: String) {
        when {
            email.isEmpty() -> {
                createAlertDialog("Merci de renseigner un email")
            }
            password.isEmpty() -> {
                createAlertDialog("Merci de renseigner un mot de passe")
            }
            else -> {
                // [START sign_in_with_email]
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG, "signInWithEmail:success")
                                goToListBooks()
                            } else {
                                Log.w(TAG, "signInWithEmail:failure", task.exception)
                                createAlertDialog("Email ou mot de passe incorrecte")
                            }
                        }
                // [END sign_in_with_email]
            }
        }
    }

    private fun createAlertDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Erreur d'identification")
        builder.setMessage(message)
        builder.show()
    }

    private fun goToListBooks() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun reload() {

    }
}