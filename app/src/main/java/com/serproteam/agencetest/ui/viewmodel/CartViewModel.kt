package com.serproteam.agencetest.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.serproteam.agencetest.data.model.Product
import com.serproteam.agencetest.domain.usecase.GetProductUseCase

class CartViewModel : ViewModel() {
    val Cart = MutableLiveData<ArrayList<Product>>()

    fun getProducts(): ArrayList<Product> {
        return GetProductUseCase().getProduct()
    }

    fun addProduct(product: ArrayList<Product>, context: Context) {
        this.Cart.postValue(product!!)
    }

}