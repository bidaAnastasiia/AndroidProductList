package com.example.productlist

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.productlist.databinding.AddElementActivityBinding
import com.example.productlist.databinding.ProductListActivityBinding
import kotlinx.android.synthetic.main.add_element_activity.*
import kotlinx.android.synthetic.main.list_element.view.*

class AddElementActivity : AppCompatActivity() {
    private lateinit var sp: SharedPreferences
    lateinit var layout: ConstraintLayout
    lateinit var listButton: ArrayList<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = AddElementActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val binding1 = ProductListActivityBinding.inflate(layoutInflater)

        layout = binding.clayout
        listButton = ArrayList()
        listButton.addAll(arrayListOf(binding.bAddProduct,binding.bEdit,binding.bRemove))
        sp = this.getSharedPreferences("mySharedPrederences", Context.MODE_PRIVATE)

        val keyvalue = intent.getStringExtra("key")
        var current_product = intent.getParcelableExtra<Product>("current")
        if (keyvalue.equals("add"))
            bAddProduct.visibility = View.VISIBLE
        else  {
            bEdit.visibility = View.VISIBLE
            bRemove.visibility = View.VISIBLE
            if (current_product != null) {
                binding.etName.setText(current_product.name)
                binding.etPrice.setText(current_product.price)
                binding.etAmount.setText(current_product.amount.toString())
                binding.cbIsBought.isChecked = current_product.isBought
            }
        }

        val productViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )
            .get(ProductViewModel::class.java)
        productViewModel.allProducts.observe(this, Observer { products ->
            products?.let {
                (binding1.rv1.adapter as MyListAdapter).setProducts(it)
            }
        })
        binding1.rv1.adapter = MyListAdapter(this, productViewModel)
        binding.bAddProduct.setOnClickListener {
            val n = binding.etName.text.toString()
            val p= binding.etPrice.text.toString()
            val a = binding.etAmount.text.toString()
            if(n != "" && p != "" && a !="")
            {
                val product = Product(
                    name = n,
                    price = p,
                    amount = a.toInt(),
                    isBought = binding.cbIsBought.isChecked
                )
                productViewModel.insert(product)
            }

            val intent = Intent(baseContext, ProductListActivity::class.java)
            startActivity(intent)
        }

        binding.bEdit.setOnClickListener {
            if (current_product != null) {
                current_product.name = binding.etName.text.toString()
                current_product.price = binding.etPrice.text.toString()
                current_product.amount = binding.etAmount.text.toString().toInt()
                current_product.isBought = binding.cbIsBought.isChecked
                productViewModel.update(current_product)
            }
            val intent = Intent(baseContext, ProductListActivity::class.java)
            startActivity(intent)
        }

        binding.bRemove.setOnClickListener {
            if (current_product != null) {
                productViewModel.delete(current_product)
            }
            val intent = Intent(baseContext, ProductListActivity::class.java)
            startActivity(intent)
        }

        binding.bRemove.setOnLongClickListener{
            productViewModel.deleteAll()
            val intent = Intent(baseContext, ProductListActivity::class.java)
            startActivity(intent)
            true
        }
    }

    override fun onStart() {
        super.onStart()
        layout.setBackgroundColor(Color.parseColor(sp.getString("background","#FFFFFF").toString()))
        var typefaceNew: Typeface = Typeface.DEFAULT
        when(sp.getString("buttonTextStyle","DEFAULT").toString()){
            "Bold" -> typefaceNew = Typeface.DEFAULT_BOLD
            "Serif" -> typefaceNew = Typeface.SERIF
        }
        for(i in listButton)
            i.typeface = typefaceNew
    }
}