package com.epsi.shopshoes

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

private var userChoice: ArrayList<Shoes> = arrayListOf()
private var lastActivity: Int = 0

class ProfilActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)

        val tvLogin: TextView = findViewById(R.id.tvLogin)
        val user = FirebaseAuth.getInstance().currentUser
        tvLogin.text = user!!.email

        // On récupère les data de l'activité précédente
        val bundle: Bundle? = intent.extras
        if(bundle != null) {
            lastActivity = bundle.getInt("activity_id")
            userChoice = bundle.getSerializable("user_choice") as ArrayList<Shoes>
        }
    }

    // Fonction qui gère le passage des data vers l'activité MainActivity.kt
    private fun goToCaddyActivity(userChoice: ArrayList<Shoes>) {
        val intent = Intent(this, CaddyActivity::class.java)
        intent.putExtra("user_choice", userChoice)
        intent.putExtra("activity_id", DETAIL_ACTIVITY)
        startActivity(intent)
    }

    // Fonction qui gère le passage des data vers l'activité CaddyActivity.kt
    private fun goToMainActivity(userChoice: ArrayList<Shoes>) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("user_choice", userChoice)
        intent.putExtra("activity_id", DETAIL_ACTIVITY)
        startActivity(intent)
    }

    // Fonction qui gère le sign_out
    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    // Création du menu en haut à droite (voir ./menu/menu_main.xml)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Fonction qui gère la selection clique pour le menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId)  {
            R.id.btnBack -> goToMainActivity(userChoice)
            R.id.btnMyCaddy -> goToCaddyActivity(userChoice)
            R.id.btnDeconnexion -> {
                FirebaseAuth.getInstance().signOut()
                goToLogin()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}