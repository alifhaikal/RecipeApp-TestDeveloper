package my.dev.recipeapp_testdeveloper

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class SpinnerCustomAdapter(val context: Context, val recipeTypeArr: Array<RecipeType>, val fromActionBar: Boolean): BaseAdapter() {

    val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return recipeTypeArr.size
    }

    override fun getItem(index: Int): Any {
        return recipeTypeArr.get(index)
    }

    override fun getItemId(index: Int): Long {
        return recipeTypeArr.get(index).id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ItemRowHolder

        if(convertView == null) {
            view = inflater.inflate(R.layout.custom_spinner, parent, false)
            viewHolder = ItemRowHolder(view)
            view?.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ItemRowHolder
        }

        viewHolder.label.text = recipeTypeArr.get(position).name

        if(fromActionBar) {
            viewHolder.label.setTextColor(Color.WHITE)
        } else {
            viewHolder.label.setTextColor(Color.DKGRAY)
        }

        return view
    }

    private class ItemRowHolder(view: View?) {
        val label: TextView
        init {
            this.label = view?.findViewById(R.id.tvSpinner) as TextView
        }
    }
}