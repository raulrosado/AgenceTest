package com.serproteam.agencetest.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.serproteam.agencetest.R
import com.serproteam.agencetest.data.model.Product

class ProductsAdapter(
    private val context: Context, private val productArray: ArrayList<Product>,
    private val itemClickListener: OnProductlickListener
) :
    RecyclerView.Adapter<BaseViewHolder<*>>() {

    interface OnProductlickListener {
        fun onProductClickListener(item: Product, context: Context, position: Int)
        fun onProductAddClickListener(item: Product, context: Context, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return MainViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_product, parent, false)
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is MainViewHolder -> holder.bind(productArray[position], position)
        }
    }

    override fun getItemCount(): Int {
        return productArray.size
    }

    inner class MainViewHolder(val view: View) : BaseViewHolder<Int>(view) {
        override fun bind(item: Product, position: Int) {
            val uri = "@drawable/" + item.image.toString()
            Log.v("raul", uri.toString());
            Log.v("raul", item.toString());
            val imageResource: Int =
                context.getResources().getIdentifier(uri, null, context.getPackageName())
            view.findViewById<ImageView>(R.id.imgProduct).setImageResource(imageResource)
            view.findViewById<TextView>(R.id.nameProduct).text = item.name
            view.findViewById<TextView>(R.id.priceProduct).text = item.price

            view.findViewById<LinearLayout>(R.id.cardProduct).setOnClickListener {
                itemClickListener.onProductClickListener(item, context, position)
            }
            view.findViewById<Button>(R.id.btnAddCart).setOnClickListener {
                itemClickListener.onProductAddClickListener(item, context, position)
            }
        }
    }
}