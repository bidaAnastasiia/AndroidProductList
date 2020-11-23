package com.example.productlist

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.productlist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var sp: SharedPreferences
    lateinit var layout: ConstraintLayout
    lateinit var listButton: ArrayList<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        layout = binding.constraintLayout
        listButton = ArrayList()
        listButton.addAll(arrayListOf(binding.buttonGoToList,binding.buttonGoToOptions))
        sp = this.getSharedPreferences("mySharedPrederences", Context.MODE_PRIVATE)
    }

    fun clickGoToList(view: View) {
        val intent = Intent(this, ProductListActivity::class.java)
        startActivity(intent)
    }

    fun clickGoToOptions(view: View) {
        val intent = Intent(this, OptionsActivity::class.java)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        layout.setBackgroundColor(
            Color.parseColor(
                sp.getString("background", "#FFFFFF").toString()
            )
        )
        var typefaceNew: Typeface = Typeface.DEFAULT
        when(sp.getString("buttonTextStyle","DEFAULT").toString()){
            "Bold" -> typefaceNew = Typeface.DEFAULT_BOLD
            "Serif" -> typefaceNew = Typeface.SERIF
        }
        for(i in listButton)
            i.typeface = typefaceNew
    }
}