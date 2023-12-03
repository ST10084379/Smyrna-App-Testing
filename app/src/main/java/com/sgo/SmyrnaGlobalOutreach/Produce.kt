package com.sgo.SmyrnaGlobalOutreach

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Produce : AppCompatActivity() {

    private lateinit var btnAdminLogin: Button

    // recycler view var
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemAdapter
    private lateinit var prodList: ArrayList<ProduceData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_produce)

        // recycler view

        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("produce")

        // initialize listview
        prodList = arrayListOf<ProduceData>()

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter = ItemAdapter(prodList)
        recyclerView.adapter = adapter

        // Read data from Firebase
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                prodList.clear()
                for (snapshot in dataSnapshot.children) {
                    val item = snapshot.getValue(ProduceData::class.java)
                    item?.let { prodList.add(it) }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
            }
        })

        val adminLoginButton = findViewById<FloatingActionButton>(R.id.adminLogin)

        adminLoginButton.setOnClickListener{
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }



    }
}