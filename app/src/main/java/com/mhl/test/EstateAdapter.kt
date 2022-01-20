package com.mhl.test

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.squareup.picasso.Picasso

class EstateAdapter(var context: Context, var estateList : ArrayList<EstateObject>): RecyclerView.Adapter<EstateAdapter.MyVH>() {

    private lateinit var myListener : onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(listener: onItemClickListener){
        myListener = listener
    }


    class MyVH (itemView: View, listener : onItemClickListener) : RecyclerView.ViewHolder(itemView) {
        var image : ImageView = itemView.findViewById(R.id.estateImage)
        val cost : TextView = itemView.findViewById(R.id.estateCost)
        val address : TextView = itemView.findViewById(R.id.estateAddress)

        init {
            itemView.setOnClickListener{
                    listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVH {
        val root =LayoutInflater.from(context).inflate(R.layout.estate_adapter, parent, false)
        return MyVH(root, myListener)
    }

    override fun onBindViewHolder(holder: MyVH, position: Int) {
        Picasso.with(context).load(estateList[position].photo?.toUri()).into(holder.image)
        holder.cost.text = estateList[position].cost.toString()
        holder.address.text = estateList[position].address
    }

    override fun getItemCount(): Int {
        return estateList.size
    }
}