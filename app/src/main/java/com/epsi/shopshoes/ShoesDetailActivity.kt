package com.epsi.shopshoes

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth

private lateinit var shoes: Shoes
private lateinit var userChoice: ArrayList<Shoes>
private var last_activity: Int = 0
var DETAIL_ACTIVITY = 2

class ShoesDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shoes_detail)

        // On récupère les élément xml de activity_shoes_detail.xml
        val tvShoesName: TextView = findViewById(R.id.tvShoesName)
        val tvShoesPrice: TextView = findViewById(R.id.tvShoesPrice)
        val tvShoesDescription: TextView = findViewById(R.id.tvShoesDescription)
        val ivShoesImage: ImageView = findViewById(R.id.ivShoesImage)
        val spShoesQuantity: Spinner = findViewById(R.id.spShoesQuantity)
        val btnHome: ImageButton = findViewById(R.id.ibtnHome)
        val btnAddShoes: ImageButton = findViewById(R.id.ibtnCaddy)

        // On récupère les data de l'acitivté précédente
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            shoes = bundle.get("shoes") as Shoes
            userChoice = bundle.getParcelableArrayList<Shoes>("user_choice") as ArrayList<Shoes>
            last_activity = bundle.getInt("activity_id")
        }

        // On créé le nouveau spinner
        ArrayAdapter.createFromResource(
                this,
                R.array.numbers,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spShoesQuantity.adapter = adapter
        }

        // On initialise la view avec les attributs de l'item
        tvShoesName.text = shoes.name
        val price = shoes.price
        "$price€".also { tvShoesPrice.text = it }
        tvShoesDescription.text = shoes.description
        Glide.with(this).load(shoes.image).into(ivShoesImage)

        btnAddShoes.setOnClickListener {
            if(userChoice.isEmpty()) {

            }
            // On vérifie que l'item choisit n'a pas déjà été choisit
            if(!userChoice.contains(shoes)) {

                // On change la quantité de l'item en récuprérant la valeur du spinner
                shoes.quantity = spShoesQuantity.selectedItem.toString()
                val quantity = shoes.quantity

                // On ajoute l'item dans la liste du panier
                userChoice.add(shoes)

                // On affiche un toast pour informer l'utilisateur que l'item à bien été ajouté dans le panier
                val toast: Toast = Toast.makeText(this@ShoesDetailActivity, "Article, ajouté au panier (Quantité : $quantity)", Toast.LENGTH_LONG)
                toast.show()
            }
            else {

                // On change la quantité de l'item en récuprérant la valeur du spinner
                shoes.quantity = spShoesQuantity.selectedItem.toString()
                val quantity = shoes.quantity

                // On affiche un toast pour informer l'utilisateur que l'item est déjà dans le panier
                val toast: Toast = Toast.makeText(this@ShoesDetailActivity, "Nouvelle quantité : $quantity", Toast.LENGTH_SHORT)
                toast.show()
            }
        }

        btnHome.setOnClickListener {
            goToMainActivity(userChoice)
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

    private fun goToProfilActivity(userChoice: ArrayList<Shoes>) {
        val intent = Intent(this, ProfilActivity::class.java)
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
            R.id.btnProfil -> goToProfilActivity(userChoice)
            R.id.btnDeconnexion -> {
                FirebaseAuth.getInstance().signOut()
                goToLogin()
            }

        }
        return super.onOptionsItemSelected(item)
    }
}