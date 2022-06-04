package com.serproteam.agencetest.domain.repository

import android.content.Context
import com.serproteam.agencetest.core.TinyDB
import com.serproteam.agencetest.data.model.Product

class AddProductToCartRepository {
    fun addProductToCart(product: Product, context: Context) {
        var tinyDB = TinyDB(context)
        var cartProduct = tinyDB.getListInt("cart")
        cartProduct?.add(product.id)
        tinyDB.putListInt("cart",cartProduct!!)
    }
}