package com.sgo.SmyrnaGlobalOutreach

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

class FarmAdapter(private val imageList: List<Int>) : RecyclerView.Adapter<FarmAdapter.FarmViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FarmAdapter.FarmViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.farm_layout, parent, false)
        return FarmViewHolder(view)
    }

    override fun onBindViewHolder(holder: FarmAdapter.FarmViewHolder, position: Int) {
        holder.mImageView.setImageResource(imageList[position])
    }

    override fun getItemCount(): Int {
        return imageList.size
    }


    inner class FarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mImageView: ImageView = itemView.findViewById(R.id.farmImage)
    }
}