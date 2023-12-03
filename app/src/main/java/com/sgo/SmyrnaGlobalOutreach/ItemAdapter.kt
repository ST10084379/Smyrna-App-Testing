package com.sgo.SmyrnaGlobalOutreach

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ItemAdapter(private val items: List<ProduceData>) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.txtName)
        val desc: TextView = itemView.findViewById(R.id.txtDesc)
        val quan: TextView = itemView.findViewById(R.id.txtQuan)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.produce_item, parent, false)
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
    }

    override fun getItemCount() = items.size
}