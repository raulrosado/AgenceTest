package com.serproteam.agencetest.adapter

import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.serproteam.agencetest.R
import com.serproteam.agencetest.data.model.Product

class ProductsAdapter(
    private val context: Context,
    private val productArray: ArrayList<Product>,
    private val recyclerView: RecyclerView,
    private val itemClickListener: OnProductlickListener
) :
    RecyclerView.Adapter<BaseViewHolder<*>>() {

    private var list: MutableList<Product> = ArrayList()
    private var progressVisibility = false
    private var canLoadAgain: Boolean = true
    private var listener: LazyLoadRecyclerCallback? = null
    val VIEW_TYPE_ITEM = 0
    val VIEW_TYPE_LOADING = 1

    /**Get actual list*/
    fun getList() = list

    /**Every update on the value will reflected after 500ms*/
    fun canLazyLoadAgain(canLoadAgain: Boolean = true) {
        this.canLoadAgain = canLoadAgain
        Handler().postDelayed({
            if (!recyclerView.isComputingLayout)
                notifyItemChanged(list.size)
        }, 500)
    }

    fun addLazyLoadCallback(listener: LazyLoadRecyclerCallback) {
        this.listener = listener
    }

    /**Every list of items added will be reflected after 1 sec*/
    fun addItem(list: List<Product>) {
        Handler().postDelayed({
            val oldPos = list.size
            this.list.addAll(list)
            progressVisibility = false
            if (!recyclerView.isComputingLayout)
                notifyItemInserted(oldPos)
        }, 1000)
    }

    /**Add single item to the adapter,
     * @param item to be added
     * @param position send the position to add the item, else it will be added in last*/
    fun addItem(item: Product, position: Int? = null) {
        Handler().postDelayed({
            val oldPos = list.size
            if (position == null)
                list.add(item)
            else list.add(position, item)
            if (!recyclerView.isComputingLayout)
                notifyItemInserted(position ?: oldPos)
        }, 500)
    }

    /**Remove the item from the list*/
    fun removeItem(item: Product) = removeItem(list.indexOf(item))

    /**Remove the item from the list with position*/
    @Suppress("MemberVisibilityCanBePrivate")
    fun removeItem(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }

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
            is MainViewHolder -> holder.bind(list[position], position)
        }
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
        return if (isPositionFooter(position)) VIEW_TYPE_LOADING
        else VIEW_TYPE_ITEM
    }

    //If position is last then type is footer
    private fun isPositionFooter(position: Int): Boolean {
        return position == list.size
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

    /**Responsible for lazy loading in recycler view items
     * #onLoadMore will be invoked when user scrolls to end,
     * remember to check you have this implemented*/
    interface LazyLoadRecyclerCallback {
        fun onLoadMore()
    }
}