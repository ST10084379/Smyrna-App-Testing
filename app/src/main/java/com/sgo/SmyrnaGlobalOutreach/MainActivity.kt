package com.sgo.SmyrnaGlobalOutreach

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.cardview.widget.CardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var navHome : CardView

    private lateinit var navMissionary: CardView
    private lateinit var navDonation: CardView
    private lateinit var navProduce: CardView

    private lateinit var navTeam: CardView
    private lateinit var navContact: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // typecasting

        navHome = findViewById(R.id.nav_home)

        navMissionary = findViewById(R.id.nav_missionary)
        navDonation = findViewById(R.id.nav_donation)
        navProduce = findViewById(R.id.nav_produce)

        navTeam = findViewById(R.id.nav_team)
        navContact = findViewById(R.id.nav_contact)





}


    fun home (view: View)
    {
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
    }



    fun missionary (view: View)
    {
        val intent = Intent(this, MissionarySchool::class.java)
        startActivity(intent)
    }

    fun donation (view: View)
    {
        val intent = Intent(this, Donation::class.java)
        startActivity(intent)
    }

    fun produce (view: View)
    {
        val intent = Intent(this,Produce::class.java)
        startActivity(intent)
    }



    fun team (view: View)
    {
        val intent = Intent(this, MeetTeam::class.java)
        startActivity(intent)
    }

    fun contact (view: View)
    {
        val intent = Intent(this, ContactUs::class.java)
        startActivity(intent)
    }

    // Calls the closeAppMessage() method
    override fun onBackPressed(){
        closeAppMessage()
    }

    // Asks the user if they are sure they want to close the application
    private fun closeAppMessage() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Warning")
            .setMessage("Are you sure you want to close the application?")
            .setPositiveButton("Yes") { _, _ -> finishAffinity() }
            .setNegativeButton("No", null)
            .show()
    }
}
