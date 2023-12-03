package com.sgo.SmyrnaGlobalOutreach

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class AdminPanel : AppCompatActivity() {

    // produce var
    private val storage = Firebase.storage
    private val storageRef = storage.reference
    private val database = Firebase.database
    private val databaseRef = database.reference.child("produce")
    private var selectedImageUri: Uri? = null

    // admin var
    private lateinit var mAuth: FirebaseAuth

    // recycler view var
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemAdapterAdmin
    private lateinit var prodList: ArrayList<ProduceData>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_panel)

        // typecast buttons
        val addProduceButton = findViewById<Button>(R.id.addProduceButton)
        val addAdminButton = findViewById<Button>(R.id.addAdminButton)

        // for registering admin
        mAuth = FirebaseAuth.getInstance()

        // add produce button
        addProduceButton.setOnClickListener {
            addProduce()
        }

        // add admin button
        addAdminButton.setOnClickListener {
            addAdmin()
        }

        // RECYCLER VIEW

        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("produce")

        // initialize listview
        prodList = arrayListOf<ProduceData>()

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewAdmin)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ItemAdapterAdmin(prodList)
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
    }

    // add produce
    private fun addProduce() {
        val inflater = LayoutInflater.from(this)
        val v = inflater.inflate(R.layout.add_produce,null)
        /**set view*/
        val prodName = v.findViewById<EditText>(R.id.prodName)
        val prodDesc = v.findViewById<EditText>(R.id.prodDesc)
        val prodQuantity = v.findViewById<EditText>(R.id.prodQuantity)
        val btnSelectImage = v.findViewById<Button>(R.id.btnSelectImage)
        val btnReset = v.findViewById<Button>(R.id.btnReset)

        // select image button
        btnSelectImage.setOnClickListener {
            selectImage()
        }

        // reset button
        btnReset.setOnClickListener {
            prodName.text.clear()
            prodDesc.text.clear()
            prodQuantity.text.clear()
            Toast.makeText(this, "Reset successful", Toast.LENGTH_SHORT).show()
        }

        // addDialog created - add produce
        val addDialog = AlertDialog.Builder(this)

        addDialog.setView(v)
        addDialog.setPositiveButton("Ok"){
                dialog,_->

            // name of image
            val imageName = prodName.text.toString()

            val name = prodName.text.toString()
            val desc = prodDesc.text.toString()
            val quan = prodQuantity.text.toString()

            // if image is selected and name of image is entered
            if (selectedImageUri != null && name.isNotBlank() && desc.isNotBlank() && quan.isNotBlank()) {
                val imageRef = storageRef.child("$imageName.jpg")
                imageRef.putFile(selectedImageUri!!)
                    .addOnSuccessListener { taskSnapshot ->
                        // Get the download URL of the uploaded image
                        imageRef.downloadUrl.addOnSuccessListener { uri ->
                            // URL of uploaded image
                            val imageUrl = uri.toString()

                            // Store image name and URL in the Firebase Realtime Database
                            val imageInfo = ProduceData(name, desc, quan, imageUrl,)
                            val key = databaseRef.push().key
                            key?.let {
                                databaseRef.child(it).setValue(imageInfo)
                                Toast.makeText(this, "Produce saved", Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            }

                            // Handle success
                            // You can add a success message here
                        }
                    }
                    .addOnFailureListener { e ->
                        // Handle error
                        // You can display an error message here
                        Toast.makeText(this, "Error: " + e.message.toString(), Toast.LENGTH_SHORT).show()
                    }
            }
        }
        addDialog.setNegativeButton("Cancel"){
                dialog,_->
            dialog.dismiss()
            Toast.makeText(this,"Cancel", Toast.LENGTH_SHORT).show()

        }
        addDialog.create()
        addDialog.show()
    }

    // select image method
    private fun selectImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, 1)
    }

    // selected image
    // override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    //     super.onActivityResult(requestCode, resultCode, data)

    //     if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
    //         selectedImageUri = data?.data
    //    }
    //}

    // selected image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            adapter.updateSelectedImageUri(selectedImageUri)
        }
    }



    // add admin method
    private fun addAdmin()
    {
        val inflater = LayoutInflater.from(this)
        val v = inflater.inflate(R.layout.add_admin,null)
        /**set view*/
        val adminUsername = v.findViewById<EditText>(R.id.username)
        val adminPassword = v.findViewById<EditText>(R.id.password)
        val adminConfirmPassword = v.findViewById<EditText>(R.id.confirmPassword)
        val btnReset = v.findViewById<Button>(R.id.btnReset)

        // reset button
        btnReset.setOnClickListener {
            adminUsername.text.clear()
            adminPassword.text.clear()
            adminConfirmPassword.text.clear()
            Toast.makeText(this, "Reset successful", Toast.LENGTH_SHORT).show()
        }

        // addDialog created - add admin
        val addDialog = AlertDialog.Builder(this)

        addDialog.setView(v)
        addDialog.setPositiveButton("Ok"){
                dialog,_->

            // store admin info
            val username = adminUsername.text.toString().trim()
            val password = adminPassword.text.toString().trim()
            val confirmPassword = adminConfirmPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                // Display an error message if any of the fields are empty
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else if (password == confirmPassword) {
                mAuth.createUserWithEmailAndPassword(username, password)
                    .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Admin registered successfully.", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        } else {
                            Toast.makeText(this, "FAILED TO REG", Toast.LENGTH_SHORT).show()
                        }
                    })
            }else {
                // Display an error message if the password and confirm password do not match
                Toast.makeText(this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT).show()
            }
        }
        addDialog.setNegativeButton("Cancel"){
                dialog,_->
            dialog.dismiss()
            Toast.makeText(this,"Cancel", Toast.LENGTH_SHORT).show()

        }
        addDialog.create()
        addDialog.show()
    }

}

