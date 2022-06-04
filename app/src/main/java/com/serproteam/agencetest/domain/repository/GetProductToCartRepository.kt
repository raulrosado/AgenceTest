package com.serproteam.agencetest.domain.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.serproteam.agencetest.core.TinyDB
import com.serproteam.agencetest.data.model.Product

class GetProductToCartRepository {
    fun getProductToCart(context: Context):ArrayList<Int> {
        var tinyDB = TinyDB(context)
        var cartProduct:ArrayList<Int> = arrayListOf<Int>()
        cartProduct = tinyDB.getListInt("cart")!!
        return cartProduct
    }
}