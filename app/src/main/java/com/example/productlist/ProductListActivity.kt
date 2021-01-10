package com.example.productlist

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.productlist.databinding.ProductListActivityBinding
import com.example.productlist.product.MyAdapter
import com.example.productlist.product.Product
import kotlinx.android.synthetic.main.product_list_activity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductListActivity : AppCompatActivity() {
    private lateinit var sp: SharedPreferences
    lateinit var layout: ConstraintLayout
    lateinit var listButton: ArrayList<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.product_list_activity)
        val binding = ProductListActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        layout = binding.clayout
        listButton = ArrayList()
        sp = this.getSharedPreferences("mySharedPrederences", Context.MODE_PRIVATE)



        binding.rv1.layoutManager = LinearLayoutManager(baseContext)
        binding.rv1.addItemDecoration(
            DividerItemDecoration(
                baseContext,
                DividerItemDecoration.VERTICAL
            )
        )

        val list = arrayListOf<Product>()
        binding.rv1.adapter =
            MyAdapter(this, list)

//        val productViewModel = ViewModelProvider(
//            this,
//            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
//        )
//            .get(ProductViewModel::class.java)
//        productViewModel.allProducts.observe(this, Observer { products ->
//            products?.let {
//                (binding.rv1.adapter as MyListAdapter).setProducts(it)
//            }
//        })
//        binding.rv1.adapter = MyListAdapter(this, productViewModel)

        fAButtonAdd.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val intent = Intent(baseContext, AddElementActivity::class.java)
                intent.putExtra("key", "add");
                finish()
                startActivity(intent)
            }
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