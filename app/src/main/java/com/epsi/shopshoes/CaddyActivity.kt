package com.epsi.shopshoes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

private lateinit var recyclerView: RecyclerView
private lateinit var adapter: CaddyAdapter
private lateinit var userChoice: ArrayList<Shoes>
private var last_activity: Int = 0
private var total: Int = 0
var CADDY_ACTIVITY = 3

class CaddyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_caddy)

        val btnHome: ImageButton = findViewById(R.id.ibtnHome)
        val btnPayment: Button = findViewById(R.id.btnPayment)


        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        val bundle: Bundle? = intent.extras
        if (bundle != null) {

            // On récupère les data de l'acitivté précédente
            userChoice = bundle.getParcelableArrayList<Shoes>("user_choice") as ArrayList<Shoes>
            last_activity = bundle.getInt("activity_id")

            adapter = CaddyAdapter(userChoice)
            recyclerView.adapter = adapter

            btnPayment.setOnClickListener {
                for(shoes: Shoes in userChoice) {
                    total += (shoes.price!!.toInt() * shoes.quantity!!.toInt())
                }
                goToPaymentActivity(userChoice, total)
            }
        }

        btnHome.setOnClickListener {
            goToMainActivity(userChoice)
        }

    }

    private fun goToMainActivity(userChoice: ArrayList<Shoes>) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("user_choice", userChoice)
        intent.putExtra("activity_id", CADDY_ACTIVITY)
        startActivity(intent)
    }

    private fun goToPaymentActivity(userChoice: ArrayList<Shoes>, total: Int) {
        val intent = Intent(this, PaymentActivity::class.java)
        intent.putExtra("user_choice", userChoice)
        intent.putExtra("total", total.toString())
        intent.putExtra("activity_id", CADDY_ACTIVITY)
        startActivity(intent)
    }

    private fun goToProfilActivity(userChoice: ArrayList<Shoes>) {
        val intent = Intent(this, ProfilActivity::class.java)
        intent.putExtra("user_choice", userChoice)
        intent.putExtra("activity_id", CADDY_ACTIVITY)
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
            R.id.btnDeconnexion -> {
                FirebaseAuth.getInstance().signOut()
                goToLogin()
            }
            R.id.btnProfil -> goToProfilActivity(userChoice)

        }
        return super.onOptionsItemSelected(item)
    }
}

