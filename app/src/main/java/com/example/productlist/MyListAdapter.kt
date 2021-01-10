package com.example.productlist

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.productlist.databinding.ListElementBinding
import com.example.productlist.product.Product
import com.example.productlist.product.ProductViewModel

class MyListAdapter (val context: Context,private val viewModel: ProductViewModel) :
    RecyclerView.Adapter<MyListAdapter.ViewHolder>() {

    private var products = emptyList<Product>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListElementBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sp = context.getSharedPreferences("mySharedPrederences", Context.MODE_PRIVATE)
        var typefaceNew: Typeface = Typeface.DEFAULT
        when(sp.getString("buttonTextStyle","DEFAULT").toString()){
            "Bold" -> typefaceNew = Typeface.DEFAULT_BOLD
            "Serif" -> typefaceNew = Typeface.SERIF
        }
        val currentProduct = products[position]
        holder.binding.tvName.text = currentProduct.name
        holder.binding.tvPrice.text = currentProduct.price + " $"
        holder.binding.tvAmount.text = currentProduct.amount.toString() + " pcs"
        holder.binding.tvIsBought.isChecked = currentProduct.isBought
        holder.binding.buttonEditProduct.setOnClickListener {
            val intent = Intent(context, AddElementActivity::class.java)
            intent.putExtra("current",currentProduct)
            startActivity(context,intent,null)
        }
        holder.binding.buttonEditProduct.typeface = typefaceNew
    }

    override fun getItemCount(): Int = products.size

    fun setProducts(products: List<Product>){
        this.products = products
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ListElementBinding):
        RecyclerView.ViewHolder(binding.root)
}