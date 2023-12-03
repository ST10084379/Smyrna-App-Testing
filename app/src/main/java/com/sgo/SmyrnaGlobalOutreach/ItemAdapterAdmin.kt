package com.sgo.SmyrnaGlobalOutreach

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ItemAdapterAdmin(private val items: List<ProduceData>) : RecyclerView.Adapter<ItemAdapterAdmin.ViewHolder>()  {

    // produce var
    private val storage = Firebase.storage
    private val storageRef = storage.reference
    private val database = Firebase.database
    private val databaseRef = database.reference.child("produce")
    private var selectedImageUri: Uri? = null
    private val PICK_IMAGE_REQUEST = 1


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.txtName)
        val desc: TextView = itemView.findViewById(R.id.txtDesc)
        val quan: TextView = itemView.findViewById(R.id.txtQuan)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val editProduce: ImageView = itemView.findViewById(R.id.editProduce)
        val deleteProduce: ImageView = itemView.findViewById(R.id.deleteProduce)
        val mMenu: ImageView = itemView.findViewById(R.id.mMenus)
        var isButtonsVisible: Boolean = false  // Flag to track button visibility

        init {
            // Initially, set the Edit and Delete buttons as invisible
            //editProduce.visibility = View.GONE
            //deleteProduce.visibility = View.GONE

            // Set a click listener for the "mMenus" button
            mMenu.setOnClickListener {
                isButtonsVisible = !isButtonsVisible
                // Toggle the visibility of Edit and Delete buttons
                editProduce.visibility = if (isButtonsVisible) View.VISIBLE else View.GONE
                deleteProduce.visibility = if (isButtonsVisible) View.VISIBLE else View.GONE
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.produce_item_admin, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.name.text = item.prodName
        holder.desc.text = item.prodDesc
        holder.quan.text = item.prodQuan

        // Load image using a library like Glide or Picasso
        // Example using Glide:
        Glide.with(holder.itemView.context).load(item.imageUrl).into(holder.imageView)

        // Set a click listener for the "Edit" button
        holder.editProduce.setOnClickListener {
            showEditProduceDialog(holder.itemView.context, item)
        }

        // Set a click listener for the "Edit" button
        holder.deleteProduce.setOnClickListener {
            showDeleteConfirmationDialog(holder.itemView.context, item)
        }

    }

    override fun getItemCount() = items.size

    private fun showEditProduceDialog(context: Context, item: ProduceData) {
        val builder = AlertDialog.Builder(context)
        val view = LayoutInflater.from(context).inflate(R.layout.add_produce, null)

        val prodNameEditText = view.findViewById<EditText>(R.id.prodName)
        val prodDescEditText = view.findViewById<EditText>(R.id.prodDesc)
        val prodQuantityEditText = view.findViewById<EditText>(R.id.prodQuantity)
        val btnSelectImage = view.findViewById<Button>(R.id.btnSelectImage)
        val btnReset = view.findViewById<Button>(R.id.btnReset)

        // select image button
        btnSelectImage.setOnClickListener {
            // Create an intent to pick an image from the gallery
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            (context as Activity).startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
        // reset button
        btnReset.setOnClickListener {
            prodNameEditText.text.clear()
            prodDescEditText.text.clear()
            prodQuantityEditText.text.clear()
            Toast.makeText(context, "Reset", Toast.LENGTH_SHORT).show()
        }
        // Populate the EditText fields with the produce details
        prodNameEditText.setText(item.prodName)
        prodDescEditText.setText(item.prodDesc)
        prodQuantityEditText.setText(item.prodQuan)
        builder.setView(view)
            .setPositiveButton("Update") { dialog, which ->
                // Get updated details from EditText
                val updatedName = prodNameEditText.text.toString()
                val updatedDesc = prodDescEditText.text.toString()
                val updatedQuantity = prodQuantityEditText.text.toString()

                // Check if an image is selected
                if (selectedImageUri != null) {
                    val imageRef = storageRef.child("$updatedName.jpg")
                    imageRef.putFile(selectedImageUri!!)
                        .addOnSuccessListener { taskSnapshot ->
                            // Get the download URL of the newly uploaded image
                            imageRef.downloadUrl.addOnSuccessListener { uri ->
                                // URL of the newly uploaded image
                                val updatedImageUrl = uri.toString()

                                // Query the database to find the item with the matching prodName
                                val query = databaseRef.orderByChild("prodName").equalTo(item.prodName)

                                query.addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        for (dataSnapshot in dataSnapshot.children) {
                                            val key = dataSnapshot.key
                                            if (key != null) {
                                                // Update the item in Firebase Realtime Database
                                                val updatedItem = ProduceData(updatedName, updatedDesc, updatedQuantity, updatedImageUrl)
                                                databaseRef.child(key).setValue(updatedItem)
                                                Toast.makeText(context, "Produce updated", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }

                                    override fun onCancelled(databaseError: DatabaseError) {
                                        // Handle database error
                                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                    }
                                })

                                // Dismiss the dialog
                                dialog.dismiss()
                            }
                        }
                } else {
                    // If no image is selected, update only the other details
                    val updatedItem = ProduceData(updatedName, updatedDesc, updatedQuantity, item.imageUrl)

                    // Query the database to find the item with the matching prodName
                    val query = databaseRef.orderByChild("prodName").equalTo(item.prodName)

                    query.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (dataSnapshot in dataSnapshot.children) {
                                val key = dataSnapshot.key
                                if (key != null) {
                                    // Update the item in Firebase Realtime Database
                                    databaseRef.child(key).setValue(updatedItem)
                                    Toast.makeText(context, "Produce updated", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Handle database error
                        }
                    })

                    // Dismiss the dialog
                    dialog.dismiss()
                }
            }
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }

        builder.create().show()

    }

    fun updateSelectedImageUri(uri: Uri?) {
        selectedImageUri = uri
    }

    private fun showDeleteConfirmationDialog(context: Context, item: ProduceData) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Delete Confirmation")
        builder.setMessage("Are you sure you want to delete this item?")

        builder.setPositiveButton("Yes") { dialog, which ->
            // Delete the item from Firebase Realtime Database and Firebase Storage
            deleteItemFromDatabaseAndStorage(context, item)

            dialog.dismiss()
        }

        builder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }

        builder.create().show()
    }

    private fun deleteItemFromDatabaseAndStorage(context: Context, item: ProduceData) {
        val imageRef = storageRef.child("${item.prodName}.jpg")
        val databaseQuery = databaseRef.orderByChild("prodName").equalTo(item.prodName)

        imageRef.delete()
            .addOnSuccessListener {
                databaseQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (dataSnapshot in dataSnapshot.children) {
                            val key = dataSnapshot.key
                            if (key != null) {
                                // Delete the item from Firebase Realtime Database
                                databaseRef.child(key).removeValue()
                                Toast.makeText(context, "Produce deleted", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle database error
                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            .addOnFailureListener {
                // Handle failure to delete from storage
                Toast.makeText(context, "Failed to delete from storage", Toast.LENGTH_SHORT).show()
            }
    }


}

// data class with produce details
data class ProduceData(
    val prodName: String? = "",
    val prodDesc: String? = "",
    val prodQuan: String? = "",
    val imageUrl: String? = ""
)

