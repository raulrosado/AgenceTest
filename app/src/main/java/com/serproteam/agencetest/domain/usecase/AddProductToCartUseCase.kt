package com.serproteam.agencetest.domain.usecase

import android.content.Context
import com.serproteam.agencetest.data.model.Product
import com.serproteam.agencetest.domain.repository.AddProductToCartRepository

class AddProductToCartUseCase {
    fun addProductToCart(product: Product,context: Context){
        var addProductToCartRepository = AddProductToCartRepository()
        addProductToCartRepository.addProductToCart(product,context)
    }
}