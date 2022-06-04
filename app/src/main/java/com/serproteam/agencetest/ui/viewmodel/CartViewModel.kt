package com.serproteam.agencetest.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.serproteam.agencetest.data.model.Product
import com.serproteam.agencetest.domain.usecase.AddProductToCartUseCase
import com.serproteam.agencetest.domain.usecase.GetProductToCartUseCase
import com.serproteam.agencetest.domain.usecase.GetProductUseCase

class CartViewModel : ViewModel() {
    var Cart = MutableLiveData<ArrayList<Int>>()

    fun getProducts(): ArrayList<Product> {
        return GetProductUseCase().getProduct()
    }

    fun getCart(context: Context): ArrayList<Int> {
        Cart.postValue(GetProductToCartUseCase().getProductToCart(context))
        Log.v("DEV", "obtengo de db:" + GetProductToCartUseCase().getProductToCart(context).size)
        return GetProductToCartUseCase().getProductToCart(context)
    }

    fun addProduct(product: Product, context: Context) {
        AddProductToCartUseCase().addProductToCart(product, context)
        Log.v("DEV", "obtengo de db:" + GetProductToCartUseCase().getProductToCart(context).size)
        Cart.postValue(GetProductToCartUseCase().getProductToCart(context))
    }

}