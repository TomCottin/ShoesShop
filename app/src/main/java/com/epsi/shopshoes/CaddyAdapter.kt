package com.epsi.shopshoes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CaddyAdapter (private val shoesList: ArrayList<Shoes>) :
        RecyclerView.Adapter<CaddyAdapter.ShoesCaddyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoesCaddyViewHolder {
        val context = parent.context
        val itemView = LayoutInflater.from(context).inflate(R.layout.caddy_item, parent, false)
        return ShoesCaddyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ShoesCaddyViewHolder, position: Int) {
        val shoes = shoesList[position]
        holder.shoesName.text = shoes.name
        holder.shoesPrice.text = shoes.price
        holder.shoesQuantity.text = shoes.quantity
        Glide.with(holder.shoesImage).load(shoes.image).into(holder.shoesImage)
    }

    override fun getItemCount(): Int = shoesList.size

    class ShoesCaddyViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {

        val shoesName: TextView = itemView.findViewById(R.id.tvShoesName)
        val shoesPrice: TextView = itemView.findViewById(R.id.tvShoesPrice)
        val shoesImage: ImageView = itemView.findViewById(R.id.ivShoesImage)
        val shoesQuantity: TextView = itemView.findViewById(R.id.tvShoesQuantity)
        val btnPay: ImageButton = itemView.findViewById(R.id.btnPay)
    }
}