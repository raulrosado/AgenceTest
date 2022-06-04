package com.serproteam.agencetest.domain.usecase

import android.content.Context
import androidx.lifecycle.LiveData
import com.serproteam.agencetest.data.model.Product
import com.serproteam.agencetest.domain.repository.AddProductToCartRepository
import com.serproteam.agencetest.domain.repository.GetProductToCartRepository

class GetProductToCartUseCase {
    fun getProductToCart(context: Context):ArrayList<Int>{
        return GetProductToCartRepository().getProductToCart(context)
    }
}