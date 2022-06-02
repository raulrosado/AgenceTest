package com.serproteam.agencetest.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.serproteam.agencetest.data.model.Product

abstract class BaseViewHolder<T>(itemView: View):RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: Product, position: Int)
}