package com.epsi.shopshoes

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

private lateinit var quantity: String
private lateinit var mListener: ShoesDetailAdapter.OnItemClickListener

class ShoesDetailAdapter (private val shoesList: ArrayList<Shoes>) :
RecyclerView.Adapter<ShoesDetailAdapter.ShoesDetailViewHolder>() {

    val userChoice: ArrayList<Shoes> = arrayListOf()

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoesDetailViewHolder {
        val context = parent.context
        val itemView = LayoutInflater.from(context).inflate(R.layout.shoes_item, parent, false)
        return ShoesDetailViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ShoesDetailViewHolder, position: Int) {

        // La variable shoes correspond à l'item cliqué contenant ses attributs
        val shoes = shoesList[position]

        // On initialise la view avec les attributs de l'item
        holder.tvShoesName.text = shoes.name
        holder.tvShoesPrice.text = shoes.price + "€"
        Glide.with(holder.ivShoesImage).load(shoes.image).into(holder.ivShoesImage)
        ArrayAdapter.createFromResource(
                holder.spQuantity.context,
                R.array.numbers,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            holder.spQuantity.adapter = adapter
        }

        val shoesNumber: Int = position + 1

        // On ajoute à l'aide de btnAddShoes l'item sélectionnée dans le panier
        holder.btnAddShoes.setOnClickListener {

            // On vérifie que l'item choisit n'a pas déjà été choisit
            if(!userChoice.contains(shoes)) {

                // On change la quantité de l'item en récuprérant la valeur du spinner
                shoes.quantity = holder.spQuantity.selectedItem.toString()
                quantity = shoes.quantity.toString()

                // On ajoute l'item dans la liste du panier
                userChoice.add(shoes)

                // On affiche un toast pour informer l'utilisateur que l'item à bien été ajouté dans le panier
                val toast: Toast = Toast.makeText(holder.itemView.context, "Article $shoesNumber, ajouté au panier (Quantité : $quantity)", Toast.LENGTH_LONG)
                toast.show()
            }
            else {

                // On affiche un toast pour informer l'utilisateur que l'item est déjà dans le panier
                val toast: Toast = Toast.makeText(holder.itemView.context, "Article déjà ajouté au panier", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }

    override fun getItemCount(): Int = shoesList.size

    class ShoesDetailViewHolder(itemView: View, listener: OnItemClickListener) :
            RecyclerView.ViewHolder(itemView) {

        // On récupère les éléments de la view (shoes_item.xml)
        val tvShoesName: TextView = itemView.findViewById(R.id.tvShoesName)
        val tvShoesPrice: TextView = itemView.findViewById(R.id.tvShoesPrice)
        val ivShoesImage: ImageView = itemView.findViewById(R.id.ivShoesImage)
        val spQuantity: Spinner = itemView.findViewById(R.id.spShoesQuantity)
        val btnAddShoes: ImageButton =  itemView.findViewById(R.id.btnAddShoes)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

    }
}