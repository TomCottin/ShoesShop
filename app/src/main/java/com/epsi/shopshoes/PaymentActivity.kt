package com.epsi.shopshoes

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

private lateinit var userChoice: ArrayList<Shoes>
var PAYMENT_ACTIVITY = 4

class PaymentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val cardNumber: EditText = findViewById(R.id.etCardNumber)
        val expirationDate: EditText = findViewById(R.id.etExpiration)
        val cryptogram: EditText = findViewById(R.id.etCrypto)
        val btnPayment: Button = findViewById(R.id.btnPayment)

        var total = "0"

        val bundle: Bundle? = intent.extras

        if (bundle != null) {
            userChoice = bundle.getParcelableArrayList<Shoes>("user_choice") as ArrayList<Shoes>
            total = bundle.getString("total").toString()
        }

        btnPayment.setOnClickListener {
            val date: List<String> = expirationDate.text.toString().split("/")
            if (cardNumber.length() != 16) {
                createAlertDialog("Merci de renseigner un numéro de carte à 16 chiffres")
            } else if (expirationDate.length() != 5
                || expirationDate.text[2].toString() != "/"
                || date[0].toInt() > 31
                || date[0].toInt() <= 0
                || date[1].toInt() > 12
                || date[0].toInt() <= 0
            ) {
                createAlertDialog("Merci de renseigner une date d'expiration valide")
            } else if (cryptogram.length() != 3) {
                createAlertDialog("Merci de renseigner un cryptogramme valide")
            } else {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Paiement")
                builder.setMessage("Montant de votre facture : $total€")
                builder.show()
            }
        }

    }

    private fun goToMainActivity(userChoice: ArrayList<Shoes>) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("user_choice", userChoice)
        intent.putExtra("activity_id", PAYMENT_ACTIVITY)
        startActivity(intent)
    }

    private fun goToProfilActivity(userChoice: ArrayList<Shoes>) {
        val intent = Intent(this, ProfilActivity::class.java)
        intent.putExtra("user_choice", userChoice)
        intent.putExtra("activity_id", PAYMENT_ACTIVITY)
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

    private fun createAlertDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Erreur de paiement")
        builder.setMessage(message)
        builder.show()
    }
}