package com.serproteam.agencetest.domain.repository

import android.content.Context
import com.serproteam.agencetest.core.TinyDB
import com.serproteam.agencetest.data.model.Product

class AddProductToCartRepository {
    fun addProductToCart(product: Product, context: Context) {
        var tinyDB = TinyDB(context)
    }
}