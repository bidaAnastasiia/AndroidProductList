package com.example.productlist

import android.content.*
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
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
import kotlinx.coroutines.runBlocking

class AddElementActivity : AppCompatActivity() {
    private lateinit var sp: SharedPreferences
    lateinit var layout: ConstraintLayout
    lateinit var listButton: ArrayList<Button>
    private var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = AddElementActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val binding1 = ProductListActivityBinding.inflate(layoutInflater)

        layout = binding.clayout
        listButton = ArrayList()
        listButton.addAll(arrayListOf(binding.bAddProduct,binding.bEdit,binding.bRemove))
        sp = this.getSharedPreferences("mySharedPrederences", Context.MODE_PRIVATE)

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

        val keyvalue = intent.getStringExtra("key")
        var current_product = intent.getParcelableExtra<Product>("current")
        if (keyvalue.equals("add"))
            bAddProduct.visibility = View.VISIBLE
        else  {
            bEdit.visibility = View.VISIBLE
            bRemove.visibility = View.VISIBLE
            if (keyvalue.equals("edit")) {
                current_product =
                    productViewModel.findById(intent.getLongExtra("added_productId", 0))
            }
            if (current_product != null)
            {
                binding.etName.setText(current_product?.name)
                binding.etPrice.setText(current_product?.price)
                binding.etAmount.setText(current_product?.amount.toString())
                binding.cbIsBought.isChecked = current_product!!.isBought
            }else
                binding.etName.setText(intent.getLongExtra("added_productId", 0).toString())
        }

        val broadcast = Intent()
        broadcast.action = getString(R.string.broadcast)
        broadcast.component = ComponentName("com.example.mybroadcastreceiver","com.example.mybroadcastreceiver.ProductReceiver")

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
                val prId = productViewModel.insert(product)
                broadcast.putExtra("channel_id",getString(R.string.channel_id))
                broadcast.putExtra("channel_name",getString(R.string.channel_name))
                broadcast.putExtra("id",id++)
                broadcast.putExtra("product_name",product.name)
                broadcast.putExtra("added_productId",prId)
                sendBroadcast(broadcast)
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