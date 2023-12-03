package com.sgo.SmyrnaGlobalOutreach

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class GoalsAdapter (private val imageList: List<Int>) : RecyclerView.Adapter<GoalsAdapter.GoalsViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.goals_layout, parent, false)
        return GoalsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: GoalsViewHolder, position: Int) {
        holder.mImageView.setImageResource(imageList[position])
    }

    inner class GoalsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mImageView: ImageView = itemView.findViewById(R.id.goalsImage)
    }

}