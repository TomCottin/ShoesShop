package com.epsi.shopshoes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.common.math.Quantiles
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore

private lateinit var recyclerView: RecyclerView
private lateinit var shoesArrayList: ArrayList<Shoes>
private lateinit var shoesAdapter: ShoesDetailAdapter
private var userChoice: ArrayList<Shoes> = arrayListOf()
private var lastActivity: Int = 0
var MAIN_ACTIVITY = 1


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        shoesArrayList = arrayListOf()
        shoesAdapter = ShoesDetailAdapter(shoesArrayList)
        recyclerView.adapter = shoesAdapter

        // On load les data des chaussure stockées sur Firebase
        loadShoesData()

        // On récupère les data de l'activité précédente
        val bundle: Bundle? = intent.extras
        if(bundle != null) {
            lastActivity = bundle.getInt("activity_id")
            userChoice = bundle.getSerializable("user_choice") as ArrayList<Shoes>
        }
        else {
            userChoice = shoesAdapter.userChoice
        }

        // Si une partie de l'adapteur est cliqué (sauf btnAddShoes) alors on rentre dans la page de détail de la chaussure
        shoesAdapter.setOnItemClickListener(object : ShoesDetailAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                goToShoesDetail(shoesArrayList[position])
            }
        })
    }

    // Fonction qui load les data Firebase
    private fun loadShoesData() {

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        db.collection("shoes")
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        Log.e("Firestore Error", error.message.toString())
                    }
                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            shoesArrayList.add(dc.document.toObject(Shoes::class.java))
                        }
                    }
                    shoesAdapter.notifyDataSetChanged()
                }
    }

    // Création du menu en haut à droite (voir ./menu/menu_main.xml)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Fonction qui gère la selection clique pour le menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
            when (id)  {

                R.id.btnMyCaddy -> {
                    if(userChoice.isNotEmpty()) {
                        goToCaddyActivity(userChoice)
                    }
                    else {
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("Mon Panier")
                        builder.setMessage("Votre panier est vide")
                        builder.show()
                    }
                }
                R.id.btnDeconnexion -> {
                    FirebaseAuth.getInstance().signOut()
                    goToLogin()
                }
                R.id.btnProfil -> goToProfilActivity(userChoice)
            }
        return super.onOptionsItemSelected(item)
    }

    // Fonction qui gère le passage des data vers l'activité ShoesDetailActivity.kt
    private fun goToShoesDetail(shoes: Shoes) {

        val intent = Intent(this, ShoesDetailActivity::class.java)
        intent.putExtra("shoes", shoes)
        intent.putExtra("user_choice", userChoice)
        intent.putExtra("activity_id", MAIN_ACTIVITY)
        startActivity(intent)
    }

    // Fonction qui gère le passage des data vers l'activité CaddyActivity.kt
    private fun goToCaddyActivity(userChoice: ArrayList<Shoes>) {
        val intent = Intent(this, CaddyActivity::class.java)
        intent.putExtra("user_choice", userChoice)
        intent.putExtra("activity_id", MAIN_ACTIVITY)
        startActivity(intent)
    }

    private fun goToProfilActivity(userChoice: ArrayList<Shoes>) {
        val intent = Intent(this, ProfilActivity::class.java)
        intent.putExtra("user_choice", userChoice)
        intent.putExtra("activity_id", MAIN_ACTIVITY)
        startActivity(intent)
    }

    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

}
