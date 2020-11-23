package com.example.productlist

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.productlist.databinding.OptionsActivityBinding
import kotlinx.android.synthetic.main.options_activity.*

class OptionsActivity  : AppCompatActivity() {
    private lateinit var sp: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    lateinit var colorValue:String
    lateinit var typefaceValue: String
    lateinit var layout: ConstraintLayout
    lateinit var listButton: ArrayList<Button>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = OptionsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sp = getSharedPreferences("mySharedPrederences",Context.MODE_PRIVATE)
        editor = sp.edit()
        layout = binding.clayout
        listButton = ArrayList()
        listButton.add(binding.buttonSave)
        //binding.buttonSave.typeface = Typeface.SERIF

        binding.buttonSave.setOnClickListener {
            val selectedBC = rgBackground.checkedRadioButtonId
            val radioButtonColor: RadioButton = findViewById(selectedBC)
            if (radioButtonColor.text.equals("Blue"))
                colorValue = "#edfffb"
            else if(radioButtonColor.text.equals("Purple"))
                colorValue = "#fbedff"
            else if(radioButtonColor.text.equals("White"))
                colorValue = "#FFFFFF"

            val selectedTS  = rgTextStyle.checkedRadioButtonId
            val radioButtonTextStyle: RadioButton = findViewById(selectedTS)
            typefaceValue = radioButtonTextStyle.text.toString()
            onStop()
            onStart()
        }

    }
    override fun onStart() {
        super.onStart()
        colorValue = sp.getString("background","#FFFFFF").toString()
        typefaceValue = sp.getString("buttonTextStyle","DEFAULT").toString()
        layout.setBackgroundColor(Color.parseColor(colorValue))
        var typefaceNew:Typeface = Typeface.DEFAULT
        when(typefaceValue){
            "Bold" -> typefaceNew = Typeface.DEFAULT_BOLD
            "Serif" -> typefaceNew = Typeface.SERIF
        }
        for(i in listButton)
            i.typeface = typefaceNew
    }

    override fun onStop() {
        super.onStop()
        editor.putString("background",colorValue)
        editor.putString("buttonTextStyle",typefaceValue)
        editor.apply()
        editor.commit()
    }
}