package my.dev.recipeapp_testdeveloper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class LocalDB(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_RECIPE_TYPE_TABLE = ("CREATE TABLE IF NOT EXISTS " +
                TABLE_RECIPE_TYPES + "("
                + COLUMN_RECIPE_TYPE_ID + " INTEGER PRIMARY KEY," +
                COLUMN_RECIPE_TYPE_NAME
                + " TEXT" + ")")

        val CREATE_RECIPE_TABLE = ("CREATE TABLE IF NOT EXISTS " +
                TABLE_RECIPE + "("
                + COLUMN_RECIPE_ID + " INTEGER PRIMARY KEY," +
                COLUMN_RECIPE_TYPE_ID_FK
                + " INTEGER," + COLUMN_RECIPE_NAME
                + " TEXT," + COLUMN_RECIPE_IMAGEURL
                + " TEXT," + COLUMN_RECIPE_INGREDIENTS
                + " TEXT," + COLUMN_RECIPE_STEPS
                + " TEXT" + ")")

        db.run {
            this?.execSQL(CREATE_RECIPE_TYPE_TABLE)
            this?.execSQL(CREATE_RECIPE_TABLE)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        println(":::HERE??")
        db.run {
            this?.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_TYPES)
            this?.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE)
            onCreate(this)
        }
    }

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "recipe.db"
        val TABLE_RECIPE_TYPES = "recipe_types"
        val COLUMN_RECIPE_TYPE_ID = "id"
        val COLUMN_RECIPE_TYPE_NAME = "productname"

        val TABLE_RECIPE = "recipe"
        val COLUMN_RECIPE_ID = "id"
        val COLUMN_RECIPE_TYPE_ID_FK = "recipeTypeId"
        val COLUMN_RECIPE_NAME = "name"
        val COLUMN_RECIPE_IMAGEURL = "imageURL"
        val COLUMN_RECIPE_INGREDIENTS = "ingredients"
        val COLUMN_RECIPE_STEPS = "steps"
    }

    fun populateRecipeTypes(recipeTypeNameArr: Array<String>) {
        val db = this.writableDatabase

        for(recipeType in recipeTypeNameArr) {
            val values = ContentValues()
            values.put(COLUMN_RECIPE_TYPE_NAME, recipeType)

            db.insert(TABLE_RECIPE_TYPES, null, values)
        }

//        db.close()
    }

    fun showRecipeTypes(): Array<RecipeType> {

        val query =
            "SELECT * FROM $TABLE_RECIPE_TYPES"

        val db = this.writableDatabase

        val cursor = db.rawQuery(query, null)

        var recipeTypeArr: Array<RecipeType> = emptyArray()

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                var recipeType: RecipeType
                val id = Integer.parseInt(cursor.getString(0))
                val name = cursor.getString(1)

                recipeType = RecipeType(id, name)
                recipeTypeArr += recipeType

                cursor.moveToNext()
            }
        }

//        db.close()
        return recipeTypeArr
    }

    fun getRecipeType(recipeTypeID: Int): RecipeType? {
        val query = "SELECT * FROM $TABLE_RECIPE_TYPES WHERE $COLUMN_RECIPE_TYPE_ID = \"$recipeTypeID\""

        val db = this.writableDatabase

        val cursor = db.rawQuery(query, null)

        var recipeType: RecipeType? = null

        if (cursor.moveToFirst()) {
            cursor.moveToFirst()

            val id = Integer.parseInt(cursor.getString(0))
            val name = cursor.getString(1)
            recipeType = RecipeType(id, name)
            cursor.close()
        }

//        db.close()
        return recipeType
    }

    fun addRecipe(recipeArr: Array<Recipe>, recipeTypeID: Int): Boolean {
        val db = this.writableDatabase
        if(recipeArr.isNotEmpty()) {
            for(recipe in recipeArr) {
                val values = ContentValues()
                values.put(COLUMN_RECIPE_TYPE_ID_FK, recipeTypeID)
                values.put(COLUMN_RECIPE_IMAGEURL, recipe.imageURL)
                values.put(COLUMN_RECIPE_NAME, recipe.name)
                values.put(COLUMN_RECIPE_INGREDIENTS, recipe.ingredients)
                values.put(COLUMN_RECIPE_STEPS, recipe.steps)

                db.insert(TABLE_RECIPE, null, values)
            }
//            db.close()
            return true
        } else {
//            db.close()
            return false
        }
    }

    fun editRecipe(recipe: Recipe): Boolean {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(COLUMN_RECIPE_IMAGEURL, recipe.imageURL)
        values.put(COLUMN_RECIPE_NAME, recipe.name)
        values.put(COLUMN_RECIPE_INGREDIENTS, recipe.ingredients)
        values.put(COLUMN_RECIPE_STEPS, recipe.steps)

        val whereQuery = "id = ${recipe.id}"

        db.update(TABLE_RECIPE, values, whereQuery, null)
        return true
    }

    fun getRecipeList(recipeTypeID: Int): ArrayList<Recipe> {
        val query = "SELECT * FROM $TABLE_RECIPE WHERE $COLUMN_RECIPE_TYPE_ID_FK = \"$recipeTypeID\""

        val db = this.writableDatabase

        val cursor = db.rawQuery(query, null)

        var recipeArr = ArrayList<Recipe>()

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                var recipe: Recipe
                val id = Integer.parseInt(cursor.getString(0))
                val name = cursor.getString(2)
                val imageURL = cursor.getString(3)
                val ingredients = cursor.getString(4)
                val steps = cursor.getString(5)

                recipe = Recipe(id, recipeTypeID, name, imageURL, ingredients, steps)
                recipeArr.add(recipe)

                cursor.moveToNext()
            }
        }

//        db.close()

        return recipeArr
    }

    fun getRecipeDetails(recipeID: Int): Recipe? {
        val query = "SELECT * FROM $TABLE_RECIPE WHERE $COLUMN_RECIPE_ID = \"$recipeID\""

        val db = this.writableDatabase

        val cursor = db.rawQuery(query, null)

        var recipe: Recipe? = null

        if (cursor.moveToFirst()) {
            cursor.moveToFirst()

            val id = Integer.parseInt(cursor.getString(0))
            val name = cursor.getString(2)
            val recipeTypeID = Integer.parseInt(cursor.getString(1))
            val imageURL = cursor.getString(3)
            val ingredients = cursor.getString(4)
            val steps = cursor.getString(5)
            recipe = Recipe(id, recipeTypeID, name, imageURL, ingredients, steps)
            cursor.close()
        }

//        db.close()
        return recipe
    }

    fun removeRecipe(recipeID: Int): Boolean {
        val db = this.writableDatabase

        db.delete(TABLE_RECIPE, "$COLUMN_RECIPE_ID = $recipeID",
            null)

//        db.close()
        return true
    }
}