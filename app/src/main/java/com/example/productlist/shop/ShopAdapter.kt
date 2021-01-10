package com.example.productlist.shop

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.productlist.databinding.ShoplistElementBinding

class ShopAdapter(val context: Context, private val viewModel: ShopViewModel) :
    RecyclerView.Adapter<ShopAdapter.ViewHolder>() {

    private var shops = emptyList<Shop>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ShoplistElementBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentShop = shops[position]
        holder.binding.tvName.text = currentShop.name
        holder.binding.tvDescription.text = currentShop.description
        holder.binding.tvRadius.text = currentShop.radius.toString()
        holder.binding.buttonRemoveShop.setOnClickListener {
            viewModel.delete(currentShop)
            Toast.makeText(context,"Shop:${currentShop.name}  was deleted",
                Toast.LENGTH_SHORT)
        }
    }

    override fun getItemCount(): Int = shops.size

    fun setShops(shops: List<Shop>){
        this.shops = shops
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ShoplistElementBinding):
        RecyclerView.ViewHolder(binding.root)
}