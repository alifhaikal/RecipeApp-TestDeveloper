package my.dev.recipeapp_testdeveloper

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RecipeRVCustomAdapter(val context: Context, val recipeArr: ArrayList<Recipe>, var onItemClick: ((Recipe) -> Unit)?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {
        return recipeArr.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val recipe = recipeArr.get(position)
        val viewHolder = holder as RecipeHolder

        val imageURL = "${context.filesDir}/${recipe.imageURL}"
        Glide.with(context).load(imageURL).into(viewHolder.recipeItemIV)
        viewHolder.recipeItemTV.text = recipe.name

        viewHolder.itemView.setOnClickListener(View.OnClickListener {
            onItemClick?.invoke(recipe)
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        return RecipeHolder(inflater.inflate(R.layout.recipe_item_row, parent, false))
    }

    internal class RecipeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var recipeItemIV: ImageView
        var recipeItemTV: TextView


        init {
            recipeItemIV = itemView.findViewById(R.id.ivRecipeItem) as ImageView
            recipeItemTV = itemView.findViewById(R.id.tvRecipeItem) as TextView
        }
    }

}