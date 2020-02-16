package my.dev.recipeapp_testdeveloper

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import kotlinx.android.synthetic.main.activity_main.*
import org.xmlpull.v1.XmlPullParser
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.os.Handler
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private val localDB = LocalDB(this)
    var currentSelectedRecipeTypeID: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(checkRecipeTypeSelected()) {
            val intent = Intent(this, DisplayActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        } else {
            var recipeTypeArr: Array<String> = emptyArray()

            val xmlParser: XmlPullParser = this.resources.getXml(R.xml.recipetypes)
            while (xmlParser.eventType != XmlPullParser.END_DOCUMENT) {
                if(xmlParser.name == "type") {
                    val name = xmlParser.getAttributeValue(null,"name")
                    if(name != null) {
                        recipeTypeArr += name
                    }
                }
                xmlParser.next()
            }

            localDB.populateRecipeTypes(recipeTypeArr)

            spTypeRecipe.onItemSelectedListener = this
            val arrayAdapter = SpinnerCustomAdapter(this, localDB.showRecipeTypes(), false)
            spTypeRecipe.adapter = arrayAdapter
        }

        btnEnter.setOnClickListener(View.OnClickListener {
            val sharedPref = this.getSharedPreferences("RECIPE_PREF", Context.MODE_PRIVATE)
            loading.visibility = View.VISIBLE
            btnEnter.isEnabled = false

            Handler().postDelayed({
                for (i in 1..5) {
                    preAddData(i)
                }

                if(currentSelectedRecipeTypeID == 0) {
                    currentSelectedRecipeTypeID = localDB.showRecipeTypes()[0].id
                }

                sharedPref.edit().putInt("selectedRecipeTypeID", currentSelectedRecipeTypeID).commit()

                val intent = Intent(this, DisplayActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }, 500)
        })
    }

    private fun checkRecipeTypeSelected(): Boolean {
        val recipeTypeArr = localDB.showRecipeTypes()
        val sharedPref = this.getSharedPreferences("RECIPE_PREF", Context.MODE_PRIVATE)

        if(recipeTypeArr.isNotEmpty()) {
            val selectedRecipeTypeID = sharedPref.getInt("selectedRecipeTypeID", 0)
            if(selectedRecipeTypeID != 0) {
                return true
            }
        }

        return false
    }

    private fun preAddData(recipeTypeID: Int) {
        var recipeArr: Array<Recipe> = emptyArray()

        when(recipeTypeID) {
            1 -> {
                addImageToDocumentPath(R.drawable.nasi, "nasi.jpg")
                addImageToDocumentPath(R.drawable.sate, "sate.jpg")
                recipeArr += Recipe(0, currentSelectedRecipeTypeID, "Nasi Lemak", "nasi.jpg", "1. Rice\n2. Peanuts\n3. Anchovies\n4. Cucumber\n5. Coconut milk", "1. Wash the rice\n2. Put coconut milk into the rice")
                recipeArr += Recipe(0, currentSelectedRecipeTypeID, "Satay", "sate.jpg", "1. Meat\n2. Chicken\n3. Turmeric", "1. Poke the meat or chicken to stick\n2. Marinate for one day\n3. Grill the satay")
            }
            2 -> {
                addImageToDocumentPath(R.drawable.yeesang, "yeesang.jpg")
                addImageToDocumentPath(R.drawable.panmee, "panmee.jpg")
                recipeArr += Recipe(0, currentSelectedRecipeTypeID, "Yee Sang", "yeesang.jpg", "1. Orange\n2. Vegetables", "1. Put the ingredients on the plate\n2. Mix it up with chopsticks")
                recipeArr += Recipe(0, currentSelectedRecipeTypeID, "Pan Mee", "panmee.jpg", "1. Noodles\n2. Vegetables\n3. Meat", "1. Wash the vegetables and noodles\n2. Add soy for the flavors")
            }
            3 -> {
                addImageToDocumentPath(R.drawable.briyani, "briyani.jpg")
                addImageToDocumentPath(R.drawable.tosai, "tosai.jpg")
                recipeArr += Recipe(0, currentSelectedRecipeTypeID, "Biryani", "briyani.jpg", "1. Basmati rice\n2. 1kg chicken\n3. 4 green chilli", "1. Marinate chicken for 20-30 minutes\n2. Cook on low heat for 5-6 minutes")
                recipeArr += Recipe(0, currentSelectedRecipeTypeID, "Tosai", "tosai.jpg", "1. Half cup of dhal\n2. 1 cup rice", "1. Firstly add dhal to large bowl\n2. Rinse them with several water")
                }
        }

        localDB.addRecipe(recipeArr, recipeTypeID)
    }

    private fun addImageToDocumentPath(imageDrawable: Int, imageFileName: String) {
        val bm = BitmapFactory.decodeResource(this.resources, imageDrawable)
        val file = File(this.filesDir, imageFileName)

        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)

            bm.compress(Bitmap.CompressFormat.JPEG, 70, fos)

            fos.close()
        } catch (e: IOException) {
            if (fos != null) {
                try {
                    fos.close()
                } catch (e1: IOException) {
                    e1.printStackTrace()
                }
            }
        }
    }

    override fun onItemSelected(av: AdapterView<*>?, view: View?, index: Int, id: Long) {
        currentSelectedRecipeTypeID = id.toInt()
    }

    override fun onNothingSelected(av: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}